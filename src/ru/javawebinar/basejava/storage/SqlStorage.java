package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlConsumer;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.sql.SqlPsConsumer;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        String sqlResume = """
                UPDATE resume
                   SET full_name = ?
                 WHERE uuid = ?
                """;
        String sqlContact = """
                UPDATE contact
                   SET value = ?
                 WHERE resume_uuid = ?
                   AND type = ?
                """;
        String uuid = r.getUuid();
        sqlHelper.transactionalExecute(conn -> {
            execute(conn, sqlResume, ps -> {
                set(ps, r.getFullName(), uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            });
            execute(conn, sqlContact, r.getContacts().entrySet(), (ps, e) ->
                    set(ps, e.getValue(), uuid, e.getKey().name())
            );
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        String sqlResume = """
                INSERT INTO resume (uuid, full_name)
                VALUES (?,?)
                """;
        String sqlContact = """
                INSERT INTO contact (type, value, resume_uuid)
                VALUES (?,?,?)
                """;
        String uuid = r.getUuid();
        sqlHelper.transactionalExecute(conn -> {
            execute(conn, sqlResume, ps -> {
                set(ps, uuid, r.getFullName());
                ps.execute();
            });
            execute(conn, sqlContact, r.getContacts().entrySet(), (ps, e) ->
                    set(ps, e.getKey().name(), e.getValue(), uuid)
            );
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        String sql = """
                SELECT *
                  FROM resume AS r
                       LEFT JOIN contact AS c
                       ON r.uuid = c.resume_uuid
                 WHERE r.uuid = ?
                """;
        return sqlHelper.execute(sql, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return addContacts(rs, newResume(rs));
        });
    }

    @Override
    public void delete(String uuid) {
        String sql = """
                DELETE
                  FROM resume
                 WHERE uuid = ?
                """;
        sqlHelper.execute(sql, ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        String sqlResume = """
                SELECT full_name, uuid
                  FROM resume
                 ORDER BY full_name, uuid
                """;
        String sqlContact = """
                SELECT type, value
                  FROM resume AS r
                       LEFT JOIN contact AS c
                       ON r.uuid = c.resume_uuid
                 ORDER BY r.full_name, r.uuid
                """;
        return sqlHelper.transactionalExecute(conn -> {
            List<Resume> resumes = new ArrayList<>();
            forEach(conn, sqlResume, rs -> resumes.add(newResume(rs)));
            forEach(conn, sqlContact, rs -> {
                for (Resume r : resumes) {
                    addContacts(rs, r);
                }
            });
            return resumes;
        });
    }

    @Override
    public int size() {
        String sql = """
                SELECT COUNT(uuid)
                  FROM resume
                """;
        return sqlHelper.execute(sql, ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    private void execute(Connection conn, String sql, SqlConsumer<PreparedStatement> action) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            action.run(ps);
        }
    }

    private <T> void execute(Connection conn, String sql, Collection<T> collection,
                             SqlPsConsumer<T> action) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (T item : collection) {
                action.run(ps, item);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void set(PreparedStatement ps, String... values) throws SQLException {
        int size = values.length;
        for (int i = 0; i < size; i++) {
            ps.setString(i + 1, values[i]);
        }
    }

    private Resume newResume(ResultSet rs) throws SQLException {
        return new Resume(rs.getString("uuid"), rs.getString("full_name"));
    }

    private Resume addContacts(ResultSet rs, Resume r) throws SQLException {
        for (Field type : r.getContacts().getClass().getDeclaredFields()) {
            ContactType t = ContactType.valueOf(rs.getString("type"));
            r.setContact(t, rs.getString("value"));
            rs.next();
        }
        return r;
    }

    private void forEach(Connection conn, String sql, SqlConsumer<ResultSet> action) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                action.run(rs);
            }
        }
    }
}

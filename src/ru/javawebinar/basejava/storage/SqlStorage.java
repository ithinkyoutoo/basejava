package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        sqlHelper.transactionalExecute(conn -> {
            String uuid = r.getUuid();
            Resume dbResume = new Resume(uuid);
            String sql = """
                    SELECT r.full_name, c.type, c.value
                      FROM resume AS r
                           LEFT JOIN contact AS c
                           ON r.uuid = c.resume_uuid
                     WHERE r.uuid = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                String rFullName = r.getFullName();
                if (!rFullName.equals(rs.getString("full_name"))) {
                    try (PreparedStatement updateName = conn.prepareStatement(
                            "UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                        updateName.setString(1, rFullName);
                        updateName.setString(2, uuid);
                        updateName.executeUpdate();
                    }
                }
                dbResume.setFullName(rFullName);
                do {
                    ContactType dbType = ContactType.valueOf(rs.getString("type"));
                    dbResume.setContact(dbType, rs.getString("value"));
                } while (rs.next());
            }
            Map<ContactType, String> dbContacts = dbResume.getContacts();
            Map<ContactType, String> rContacts = r.getContacts();
            try (PreparedStatement del = conn.prepareStatement(
                    "DELETE FROM contact WHERE type = ? AND resume_uuid = ?")) {
                for (Map.Entry<ContactType, String> e : dbContacts.entrySet()) {
                    ContactType dbType = e.getKey();
                    if (!rContacts.containsKey(dbType)) {
                        del.setString(1, dbType.toString());
                        del.setString(2, uuid);
                        del.addBatch();
                        dbResume.getContacts().remove(dbType);
                    }
                }
                del.executeBatch();
            }
            try (PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO contact (type, value, resume_uuid) VALUES (?, ?, ?)")) {
                for (Map.Entry<ContactType, String> e : rContacts.entrySet()) {
                    ContactType rType = e.getKey();
                    String rValue = e.getValue();
                    if (!dbContacts.containsKey(rType)) {
                        insert.setString(1, rType.toString());
                        insert.setString(2, rValue);
                        insert.setString(3, uuid);
                        insert.addBatch();
                        dbResume.setContact(rType, rValue);
                    }
                }
                insert.executeBatch();
            }
            try (PreparedStatement update = conn.prepareStatement(
                    "UPDATE contact SET value = ? WHERE resume_uuid = ? AND type = ?")) {
                for (Map.Entry<ContactType, String> e : rContacts.entrySet()) {
                    ContactType rType = e.getKey();
                    String rValue = e.getValue();
                    String dbValue = dbResume.getContacts().get(rType);
                    if (!dbValue.equals(rValue)) {
                        update.setString(1, rValue);
                        update.setString(2, uuid);
                        update.setString(3, rType.toString());
                        update.addBatch();
                    }
                }
                update.executeBatch();
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        String uuid = r.getUuid();
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, uuid);
                ps.setString(2, r.getFullName());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO contact (type, value, resume_uuid) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                    ps.setString(1, e.getKey().name());
                    ps.setString(2, e.getValue());
                    ps.setString(3, uuid);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
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
            Resume r = new Resume(rs.getString("uuid"), rs.getString("full_name"));
            do {
                ContactType type = ContactType.valueOf(rs.getString("type"));
                r.setContact(type, rs.getString("value"));
            } while (rs.next());
            return r;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> resumes = new HashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT full_name, uuid FROM resume")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            List<Resume> sortResumes = new ArrayList<>(resumes.size());
            String sql = """
                    SELECT type, value, uuid
                      FROM resume AS r
                           LEFT JOIN contact AS c
                           ON r.uuid = c.resume_uuid
                     ORDER BY r.full_name, r.uuid
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                rs.next();
                int size = resumes.size();
                for (int i = 0; i < size; i++) {
                    String uuid = rs.getString("uuid");
                    Resume r = resumes.get(uuid);
                    do {
                        if (!uuid.equals(rs.getString("uuid"))) {
                            break;
                        }
                        ContactType type = ContactType.valueOf(rs.getString("type"));
                        r.setContact(type, rs.getString("value"));
                    } while (rs.next());
                    sortResumes.add(r);
                }
            }
            return sortResumes;
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT COUNT(uuid) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }
}

package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }
            deleteContacts(conn, r);
            insertContacts(conn, r);
            deleteSections(conn, r);
            insertSections(conn, r);
            return null;
        });
    }

    private void deleteContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void deleteSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContacts(conn, r);
            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        String sql = """
                SELECT r.full_name, c.type, c.value
                  FROM resume AS r
                       LEFT JOIN contact AS c
                       ON r.uuid = c.resume_uuid
                 WHERE r.uuid = ?
                 UNION
                SELECT r.full_name, s.type, s.value
                  FROM resume AS r
                       LEFT JOIN section AS s
                       ON r.uuid = s.resume_uuid
                 WHERE r.uuid = ?;
                """;
        return sqlHelper.execute(sql, ps -> {
            ps.setString(1, uuid);
            ps.setString(2, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                String type = rs.getString("type");
                if (type != null) {
                    try {
                        ContactType.valueOf(type);
                        addContact(rs, r);
                    } catch (IllegalArgumentException e) {
                        addSection(rs, r);
                    }
                }
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
            Map<String, Resume> resumes = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT uuid, full_name FROM resume ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return List.of();
                }
                do {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                } while (rs.next());
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT type, value, resume_uuid FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    final Resume r = resumes.get(rs.getString("resume_uuid"));
                    addContact(rs, r);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT type, value, resume_uuid FROM section")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    final Resume r = resumes.get(rs.getString("resume_uuid"));
                    addSection(rs, r);
                }
                return new ArrayList<>(resumes.values());
            }
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

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO contact (type, value, resume_uuid) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, e.getKey().name());
                ps.setString(2, e.getValue());
                ps.setString(3, r.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO section (type, value, resume_uuid) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                SectionType type = e.getKey();
                ps.setString(1, type.name());
                ps.setString(2, toString(type, e.getValue()));
                ps.setString(3, r.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private String toString(SectionType type, Section section) {
        return switch (type) {
            case OBJECTIVE, PERSONAL -> ((TextSection) section).getText();
            case ACHIEVEMENT, QUALIFICATIONS -> ((ListSection) section).getItems().stream()
                    .reduce("", (str, item) -> str + item + '\n');
            case EXPERIENCE, EDUCATION -> "";
        };
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        r.setContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        SectionType type = SectionType.valueOf(rs.getString("type"));
        String value = rs.getString("value");
        switch (type) {
            case OBJECTIVE, PERSONAL -> r.setSection(type, new TextSection(value));
            case ACHIEVEMENT, QUALIFICATIONS -> r.setSection(type, new ListSection(value));
        }
    }
}

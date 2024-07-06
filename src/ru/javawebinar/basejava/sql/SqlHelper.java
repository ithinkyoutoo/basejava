package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public record SqlHelper(ConnectionFactory connectionFactory) {

    private static final int SQLSTATE_UNIQUE_VIOLATION = 23505;

    public <T> T execute(String uuid, String sql, SqlSupplier action) {
        T result = execute(sql, action);
        if (result.equals(false)) {
            throw new NotExistStorageException(uuid);
        }
        return result;
    }

    public <T> T execute(String sql, SqlSupplier action) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return (T) action.get(ps);
        } catch (SQLException e) {
            if (Integer.parseInt(e.getSQLState()) == SQLSTATE_UNIQUE_VIOLATION) {
                throw new ExistStorageException(e);
            }
            throw new StorageException(e);
        }
    }
}

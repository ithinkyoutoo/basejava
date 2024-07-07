package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public record SqlHelper(ConnectionFactory connectionFactory) {

    private static final int SQLSTATE_UNIQUE_VIOLATION = 23505;

    public <T> T execute(String sql, SqlSupplier<T> action) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return action.get(ps);
        } catch (SQLException e) {
            if (Integer.parseInt(e.getSQLState()) == SQLSTATE_UNIQUE_VIOLATION) {
                throw new ExistStorageException(e);
            }
            throw new StorageException(e);
        }
    }
}

package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public record SqlHelper(ConnectionFactory connectionFactory) {

    public void execute(String sql, SqlRunnable action) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            action.run(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public <T> T executeAndGet(String sql, SqlSupplier action) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return (T) action.get(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}

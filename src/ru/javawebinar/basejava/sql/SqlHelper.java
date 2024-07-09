package ru.javawebinar.basejava.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public record SqlHelper(ConnectionFactory connectionFactory) {

    public <T> T execute(String sql, SqlSupplier<T> action) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return action.get(ps);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }
}

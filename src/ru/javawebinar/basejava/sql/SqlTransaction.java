package ru.javawebinar.basejava.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlTransaction<T> {

    T get(Connection conn) throws SQLException;
}

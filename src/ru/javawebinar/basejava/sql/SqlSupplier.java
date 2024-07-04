package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlSupplier<T> {

    T get(PreparedStatement ps) throws SQLException;
}

package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlPsConsumer<T> {

    void run(PreparedStatement ps, T t) throws SQLException;
}

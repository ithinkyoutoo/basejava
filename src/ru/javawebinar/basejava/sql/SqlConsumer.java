package ru.javawebinar.basejava.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlConsumer<T> {

    void run(T t) throws SQLException;
}

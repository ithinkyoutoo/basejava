package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlRunnable {

    void run(PreparedStatement ps) throws SQLException;
}

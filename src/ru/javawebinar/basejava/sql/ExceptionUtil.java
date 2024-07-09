package ru.javawebinar.basejava.sql;

import org.postgresql.util.PSQLException;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.SQLException;

public class ExceptionUtil {

    private static final int SQLSTATE_UNIQUE_VIOLATION = 23505;

    private ExceptionUtil() {
    }

    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException) {
            if (Integer.parseInt(e.getSQLState()) == SQLSTATE_UNIQUE_VIOLATION) {
                return new ExistStorageException(e);
            }
        }
        return new StorageException(e);
    }
}

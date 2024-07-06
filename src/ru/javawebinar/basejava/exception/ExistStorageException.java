package ru.javawebinar.basejava.exception;

public class ExistStorageException extends StorageException {

    public ExistStorageException(String uuid) {
        super("Resume " + uuid + " already exist", uuid);
    }

    public ExistStorageException(Exception e) {
        super("Resume already exist: " + e.getMessage(), e);
    }
}

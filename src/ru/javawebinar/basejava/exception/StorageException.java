package ru.javawebinar.basejava.exception;

import java.io.File;

public class StorageException extends RuntimeException {

    private final String uuid;

    public StorageException(File file, String message) {
        super(file.getName() + message);
        this.uuid = null;
    }

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, String uuid, Exception e) {
        super(message, e);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}

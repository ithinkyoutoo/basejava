package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serialization.ObjectStreamStrategy;

public class FileStorageObjectStreamStrategyTest extends AbstractStorageTest {

    public FileStorageObjectStreamStrategyTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}
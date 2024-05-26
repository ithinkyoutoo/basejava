package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.storage.AbstractStorageTest;

public class FileStorageObjectStreamStrategyTest extends AbstractStorageTest {

    public FileStorageObjectStreamStrategyTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}
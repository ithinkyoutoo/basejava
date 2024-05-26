package ru.javawebinar.basejava.storage;

public class FileStorageObjectStreamStrategyTest extends AbstractStorageTest {

    public FileStorageObjectStreamStrategyTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}
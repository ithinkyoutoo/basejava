package ru.javawebinar.basejava.storage;

public class PathStorageObjectStreamStrategyTest extends AbstractStorageTest {

    public PathStorageObjectStreamStrategyTest() {
        super(new PathStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}
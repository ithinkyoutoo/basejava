package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serialization.ObjectStreamStrategy;

public class PathStorageObjectStreamStrategyTest extends AbstractStorageTest {

    public PathStorageObjectStreamStrategyTest() {
        super(new PathStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}
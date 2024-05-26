package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.storage.AbstractStorageTest;

public class PathStorageObjectStreamStrategyTest extends AbstractStorageTest {

    public PathStorageObjectStreamStrategyTest() {
        super(new PathStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}
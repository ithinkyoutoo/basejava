package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serialization.DataStreamStrategy;

public class PathStorageDataStreamStrategyTest extends AbstractStorageTest {

    public PathStorageDataStreamStrategyTest() {
        super(new PathStorage(STORAGE_DIR, new DataStreamStrategy()));
    }
}

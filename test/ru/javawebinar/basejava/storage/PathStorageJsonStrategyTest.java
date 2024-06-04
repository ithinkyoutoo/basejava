package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serialization.JsonStrategy;

public class PathStorageJsonStrategyTest extends AbstractStorageTest {

    public PathStorageJsonStrategyTest() {
        super(new PathStorage(STORAGE_DIR, new JsonStrategy()));
    }
}

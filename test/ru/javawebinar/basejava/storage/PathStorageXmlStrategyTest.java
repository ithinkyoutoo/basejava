package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serialization.XmlStrategy;

public class PathStorageXmlStrategyTest extends AbstractStorageTest {

    public PathStorageXmlStrategyTest() {
        super(new PathStorage(STORAGE_DIR, new XmlStrategy()));
    }
}

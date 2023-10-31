package ru.javawebinar.basejava.storage;

import org.junit.Test;

public class MapHashStorageTest extends AbstractStorageTest {

    public MapHashStorageTest() {
        super(new MapHashStorage());
    }

    @Override
    @Test
    public void storageOverflow() {
    }
}
package ru.javawebinar.basejava.storage;

import org.junit.Test;

public class MapResumeStorageTest extends AbstractStorageTest {

    public MapResumeStorageTest() {
        super(new MapResumeStorage());
    }

    @Override
    @Test
    public void storageOverflow() {
    }
}
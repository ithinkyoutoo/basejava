package ru.javawebinar.basejava.storage;

import org.junit.Test;

public class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Override
    @Test
    public void storageOverflow() {
    }
}
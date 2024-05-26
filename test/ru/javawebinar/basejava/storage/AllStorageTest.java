package ru.javawebinar.basejava.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.javawebinar.basejava.storage.serialization.FileStorageObjectStreamStrategyTest;
import ru.javawebinar.basejava.storage.serialization.PathStorageObjectStreamStrategyTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ListStorageTest.class,
        MapStorageTest.class,
        MapResumeStorageTest.class,
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ObjectStreamFileStorageTest.class,
        ObjectStreamPathStorageTest.class,
        FileStorageObjectStreamStrategyTest.class,
        PathStorageObjectStreamStrategyTest.class
})
public class AllStorageTest {
}
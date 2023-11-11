package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.fail;
import static ru.javawebinar.basejava.storage.AbstractArrayStorage.CAPACITY;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void storageOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < CAPACITY; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            fail("Переполнение произошло раньше времени");
        }
        storage.save(new Resume());
    }
}
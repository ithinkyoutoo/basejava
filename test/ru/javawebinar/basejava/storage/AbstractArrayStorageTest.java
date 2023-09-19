package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest {

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private final Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
        assertEquals(0, storage.getAll().length);
    }

    @Test
    public void update() {
        Resume resume = storage.get(UUID_1);
        storage.update(storage.get(UUID_1));
        assertEquals(resume, storage.get(UUID_1));
    }

    @Test
    public void save() {
        storage.save(new Resume(UUID_4));
        assertEquals(4, storage.size());
        assertEquals(4, storage.getAll().length);
        storage.get(UUID_4);
    }

    @Test
    public void get() {
        Resume[] resumes = storage.getAll();
        assertEquals(resumes[0], storage.get(UUID_1));
        assertEquals(resumes[1], storage.get(UUID_2));
        assertEquals(resumes[2], storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_2);
        assertEquals(2, storage.size());
        assertEquals(2, storage.getAll().length);
        storage.get(UUID_2);
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        assertEquals(3, resumes.length);
        assertEquals(resumes[0], storage.get(UUID_1));
        assertEquals(resumes[1], storage.get(UUID_2));
        assertEquals(resumes[2], storage.get(UUID_3));
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test(expected = ExistStorageException.class)
    public void getExist() {
        storage.save(new Resume(UUID_1));
    }

    @Test(expected = StorageException.class)
    public void storageOverflow() {
        try {
            for (int i = 3; i < 10_000; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            fail("Переполнение произошло раньше времени");
        }
        storage.save(new Resume());
    }
}
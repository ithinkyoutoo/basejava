package ru.javawebinar.basejava.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.List;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest {

    private static final String DUMMY = "dummy";
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String FULL_NAME_1 = "name1";
    private static final String FULL_NAME_2 = "name2";
    private static final String FULL_NAME_3 = "name3";
    private static final String FULL_NAME_4 = "name4";
    private static final Resume RESUME_1 = new Resume(UUID_1, FULL_NAME_1);
    private static final Resume RESUME_2 = new Resume(UUID_2, FULL_NAME_2);
    private static final Resume RESUME_3 = new Resume(UUID_3, FULL_NAME_3);
    private static final Resume RESUME_4 = new Resume(UUID_4, FULL_NAME_4);

    protected final Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @After
    public void resetFullName() {
        RESUME_1.setFullName(FULL_NAME_1);
        RESUME_2.setFullName(FULL_NAME_2);
        RESUME_3.setFullName(FULL_NAME_3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        assertEquals(List.of(), storage.getAllSorted());
    }

    @Test
    public void update() {
        storage.update(new Resume(UUID_1, FULL_NAME_4));
        RESUME_1.setFullName(FULL_NAME_4);
        assertEquals(RESUME_1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume(DUMMY));
    }

    @Test
    public void save() {
        int size = storage.size();
        int length = getLength();
        storage.save(RESUME_4);
        assertSize(size + 1);
        assertLength(length + 1);
        assertGet(RESUME_4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(new Resume(UUID_1, FULL_NAME_1));
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(DUMMY);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        int size = storage.size();
        int length = getLength();
        storage.delete(UUID_2);
        assertSize(size - 1);
        assertLength(length - 1);
        storage.get(UUID_2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(DUMMY);
    }

    @Test
    public void getAllSorted() {
        Resume[] expected = {RESUME_1, RESUME_2, RESUME_3};
        assertArray(expected);
        RESUME_2.setFullName(FULL_NAME_1);
        RESUME_3.setFullName(FULL_NAME_1);
        assertArray(expected);
        RESUME_1.setFullName(FULL_NAME_3);
        Resume[] expected2 = {RESUME_2, RESUME_3, RESUME_1};
        assertArray(expected2);
    }

    @Test
    public void size() {
        assertSize(getLength());
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private int getLength() {
        return storage.getAllSorted().size();
    }

    private void assertLength(int length) {
        assertEquals(length, getLength());
    }

    private void assertGet(Resume resume) {
        assertEquals(resume, storage.get(resume.getUuid()));
    }

    private void assertArray(Resume[] expected) {
        assertArrayEquals(expected, storage.getAllSorted().toArray());
    }
}
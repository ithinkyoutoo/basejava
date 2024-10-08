package ru.javawebinar.basejava.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static ru.javawebinar.basejava.model.ContactType.SKYPE;
import static ru.javawebinar.basejava.model.ContactType.TEL;
import static ru.javawebinar.basejava.model.ResumeTestData.newResume;

public abstract class AbstractStorageTest {

    protected static final File STORAGE_DIR = Config.getStorageDir();
    private static final String DUMMY = "dummy";
    private static final String UUID_1 = UUID.randomUUID().toString();
    private static final String UUID_2 = UUID.randomUUID().toString();
    private static final String UUID_3 = UUID.randomUUID().toString();
    private static final String UUID_4 = UUID.randomUUID().toString();
    private static final String FULL_NAME_1 = "name1";
    private static final String FULL_NAME_2 = "name2";
    private static final String FULL_NAME_3 = "name3";
    private static final String FULL_NAME_4 = "name4";
    private static final Resume RESUME_1 = newResume(UUID_1, FULL_NAME_1);
    private static final String T_VALUE = RESUME_1.getContact(TEL);
    private static final String S_VALUE = RESUME_1.getContact(SKYPE);
    private static final Resume RESUME_2 = newResume(UUID_2, FULL_NAME_2);
    private static final Resume RESUME_3 = newResume(UUID_3, FULL_NAME_3);
    private static final Resume RESUME_4 = newResume(UUID_4, FULL_NAME_4);
    private static final Comparator<Resume> RESUME_COMPARATOR
            = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

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
    public void resetResumes() {
        RESUME_1.setFullName(FULL_NAME_1);
        RESUME_1.setContact(TEL, T_VALUE);
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
        int size = storage.get(UUID_1).getContacts().size();
        Resume newResume = newResume(UUID_1, FULL_NAME_4);
        newResume.setContact(TEL, "+7(000) 000-0000");
        newResume.getContacts().remove(SKYPE);
        storage.update(newResume);
        assertEquals(newResume, storage.get(UUID_1));
        assertContactsSize(size - 1);
        newResume.setContact(SKYPE, S_VALUE);
        storage.update(newResume);
        assertEquals(newResume, storage.get(UUID_1));
        assertContactsSize(size);
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
        storage.save(newResume(UUID_1, FULL_NAME_1));
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
        List<Resume> expected = Arrays.asList(RESUME_1, RESUME_2, RESUME_3);
        assertList(expected);
        updateFullName(RESUME_2, FULL_NAME_1);
        updateFullName(RESUME_3, FULL_NAME_1);
        expected.sort(RESUME_COMPARATOR);
        assertList(expected);
        updateFullName(RESUME_1, FULL_NAME_3);
        expected.sort(RESUME_COMPARATOR);
        assertList(expected);
    }

    @Test
    public void size() {
        assertSize(getLength());
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertContactsSize(int size) {
        assertEquals(size, storage.get(UUID_1).getContacts().size());
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

    private void assertList(List<Resume> expected) {
        assertEquals(expected, storage.getAllSorted());
    }

    private void updateFullName(Resume resume, String name) {
        resume.setFullName(name);
        storage.update(resume);
    }
}
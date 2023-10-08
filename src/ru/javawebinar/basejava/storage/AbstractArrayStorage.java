package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int CAPACITY = 10_000;

    protected final Resume[] storage = new Resume[CAPACITY];
    protected int countResume;

    public final void clear() {
        Arrays.fill(storage, 0, countResume, null);
        countResume = 0;
    }

    public final Resume[] getAll() {
        return Arrays.copyOf(storage, countResume);
    }

    public final int size() {
        return countResume;
    }

    protected void update(Resume resume, Object searchKey) {
        storage[(int) searchKey] = resume;
    }

    protected void save(Resume resume, Object searchKey) {
        if (countResume == CAPACITY) {
            throw new StorageException("Хранилище заполнено, вы не можете добавить новое резюме", resume.getUuid());
        } else {
            saveResume(resume, (int) searchKey);
            countResume++;
        }
    }

    protected Resume get(String uuid, Object searchKey) {
        return storage[(int) searchKey];
    }

    protected void delete(String uuid, Object searchKey) {
        countResume--;
        deleteResume((int) searchKey);
        storage[countResume] = null;
    }

    protected boolean isExist(Object searchKey) {
        return (int) searchKey < 0;
    }

    protected abstract Object findIndex(String uuid);

    protected abstract void deleteResume(int index);

    protected abstract void saveResume(Resume resume, int index);
}
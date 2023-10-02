package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
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

    public final void delete(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            countResume--;
            deleteResume(index);
            storage[countResume] = null;
        }
    }

    public final Resume[] getAll() {
        return Arrays.copyOf(storage, countResume);
    }

    public final int size() {
        return countResume;
    }

    protected abstract int findIndex(String uuid);

    protected void setResume(Resume resume, int index) {
        storage[index] = resume;
    }

    protected boolean hasNotCapacity() {
        return countResume == CAPACITY;
    }

    protected abstract void saveResume(Resume resume, int index);

    protected Resume getResume(int index) {
        return storage[index];
    }

    protected abstract void deleteResume(int index);
}
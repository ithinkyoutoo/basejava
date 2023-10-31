package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int CAPACITY = 10_000;

    protected final Resume[] storage = new Resume[CAPACITY];
    protected int countResume;

    public final void clear() {
        Arrays.fill(storage, 0, countResume, null);
        countResume = 0;
    }

    public final int size() {
        return countResume;
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage[(int) searchKey] = resume;
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        if (countResume == CAPACITY) {
            throw new StorageException("Хранилище заполнено, вы не можете добавить новое резюме", resume.getUuid());
        } else {
            saveResume(resume, (int) searchKey);
            countResume++;
        }
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage[(int) searchKey];
    }

    @Override
    protected void doDelete(Object searchKey) {
        countResume--;
        deleteResume((int) searchKey);
        storage[countResume] = null;
    }

    @Override
    protected final List<Resume> doGetAll() {
        return Arrays.asList(Arrays.copyOf(storage, countResume));
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }

    @Override
    protected abstract Object findSearchKey(String uuid);

    protected abstract void deleteResume(int searchKey);

    protected abstract void saveResume(Resume resume, int searchKey);
}
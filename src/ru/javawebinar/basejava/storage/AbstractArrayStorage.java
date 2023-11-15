package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

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
    protected void doUpdate(Resume resume, Integer searchKey) {
        storage[searchKey] = resume;
    }

    @Override
    protected void doSave(Resume resume, Integer searchKey) {
        if (countResume == CAPACITY) {
            throw new StorageException("Хранилище заполнено, вы не можете добавить новое резюме", resume.getUuid());
        } else {
            saveResume(resume, searchKey);
            countResume++;
        }
    }

    @Override
    protected Resume doGet(Integer searchKey) {
        return storage[searchKey];
    }

    @Override
    protected void doDelete(Integer searchKey) {
        countResume--;
        deleteResume(searchKey);
        storage[countResume] = null;
    }

    @Override
    protected final List<Resume> doGetAll() {
        return Arrays.asList(Arrays.copyOf(storage, countResume));
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    @Override
    protected abstract Integer findSearchKey(String uuid);

    protected abstract void deleteResume(int searchKey);

    protected abstract void saveResume(Resume resume, int searchKey);
}
package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public abstract void clear();

    public final void update(Resume resume) {
        update(resume, getExistingSearchKey(resume.getUuid()));
    }

    public final void save(Resume resume) {
        save(resume, getNotExistingSearchKey(resume.getUuid()));
    }

    public final Resume get(String uuid) {
        return get(uuid, getExistingSearchKey(uuid));
    }

    public final void delete(String uuid){
        delete(uuid, getExistingSearchKey(uuid));
    }

    public abstract Resume[] getAll();

    public abstract int size();

    protected abstract void update(Resume resume, Object searchKey);

    protected abstract void save(Resume resume, Object searchKey);

    protected abstract Resume get(String uuid, Object searchKey);

    protected abstract void delete(String uuid, Object searchKey);

    protected abstract Object findIndex(String uuid);

    protected abstract boolean isExist(Object searchKey);

    private Object getExistingSearchKey(String uuid) {
        Object searchKey = findIndex(uuid);
        if (isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistingSearchKey(String uuid) {
        Object searchKey = findIndex(uuid);
        if (!isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}
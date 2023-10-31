package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    private static final Comparator<Resume> RESUME_COMPARATOR
            = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    public final void update(Resume resume) {
        doUpdate(resume, getExistingSearchKey(resume.getUuid()));
    }

    public final void save(Resume resume) {
        doSave(resume, getNotExistingSearchKey(resume.getUuid()));
    }

    public final Resume get(String uuid) {
        return doGet(getExistingSearchKey(uuid));
    }

    public final void delete(String uuid){
        doDelete(getExistingSearchKey(uuid));
    }

    public final List<Resume> getAllSorted() {
        List<Resume> r = doGetAll();
        r.sort(RESUME_COMPARATOR);
        return r;
    }

    protected abstract void doUpdate(Resume resume, Object searchKey);

    protected abstract void doSave(Resume resume, Object searchKey);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doDelete(Object searchKey);

    protected abstract List<Resume> doGetAll();

    protected abstract Object findSearchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);

    private Object getExistingSearchKey(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (isExist(searchKey)) {
            return searchKey;
        }
        throw new NotExistStorageException(uuid);
    }

    private Object getNotExistingSearchKey(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}
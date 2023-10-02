package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public abstract void clear();

    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        int index = findIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            setResume(resume, index);
        }
    }

    public final void save(Resume resume) {
        String uuid = resume.getUuid();
        int index = findIndex(uuid);
        if (hasNotCapacity()) {
            throw new StorageException("Хранилище заполнено, вы не можете добавить новое резюме", uuid);
        } else if (index >= 0) {
            throw new ExistStorageException(uuid);
        } else {
            saveResume(resume, index);
        }
    }

    public final Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return getResume(index);
    }

    public abstract void delete(String uuid);

    public abstract Resume[] getAll();

    public abstract int size();

    protected abstract int findIndex(String uuid);

    protected abstract void setResume(Resume resume, int index);

    protected abstract boolean hasNotCapacity();

    protected abstract void saveResume(Resume resume, int index);

    protected abstract Resume getResume(int index);
}
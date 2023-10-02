package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class ListStorage extends AbstractStorage {

    private final List<Resume> storage = new ArrayList<>();

    public void clear() {
        storage.clear();
    }

    public final void delete(String uuid) {
        ListIterator<Resume> iterator = storage.listIterator();
        while (iterator.hasNext()) {
            Resume resume = iterator.next();
            if (Objects.equals(resume.getUuid(), uuid)) {
                iterator.remove();
                return;
            }
        }
        throw new NotExistStorageException(uuid);
    }

    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    public int size() {
        return storage.size();
    }

    protected int findIndex(String uuid) {
        Resume resume = new Resume(uuid);
        return storage.indexOf(resume);
    }

    protected void setResume(Resume resume, int index) {
        storage.set(index, resume);
    }

    protected boolean hasNotCapacity() {
        return false;
    }

    protected void saveResume(Resume resume, int index) {
        storage.add(resume);
    }

    protected Resume getResume(int index) {
        return storage.get(index);
    }
}
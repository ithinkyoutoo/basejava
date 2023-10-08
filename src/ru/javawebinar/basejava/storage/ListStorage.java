package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private final List<Resume> storage = new ArrayList<>();

    public void clear() {
        storage.clear();
    }

    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    public int size() {
        return storage.size();
    }

    protected void update(Resume resume, Object searchKey) {
        storage.set((int) searchKey, resume);
    }

    protected void save(Resume resume, Object searchKey) {
        storage.add(resume);
    }

    protected Resume get(String uuid, Object searchKey) {
        return storage.get((int) searchKey);
    }

    protected void delete(String uuid, Object searchKey) {
        storage.remove((int) searchKey);
    }

    protected Object findIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (uuid.equals(storage.get(i).getUuid())) {
                return i;
            }
        }
        return -1;
    }

    protected boolean isExist(Object searchKey) {
        return (int) searchKey < 0;
    }
}
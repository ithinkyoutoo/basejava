package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {

    private final Map<String, Resume> storage = new HashMap<>();

    public void clear() {
        storage.clear();
    }

    public Resume[] getAll() {
        return storage.values().toArray(new Resume [0]);
    }

    public int size() {
        return storage.size();
    }

    protected void doUpdate(Resume resume, Object searchKey) {
        storage.put((String) searchKey, resume);
    }

    protected void doSave(Resume resume, Object searchKey) {
        storage.put((String) searchKey, resume);
    }

    protected Resume doGet(Object searchKey) {
        return storage.get((String) searchKey);
    }

    protected void doDelete(Object searchKey) {
        storage.remove((String) searchKey);
    }

    protected Object findSearchKey(String uuid) {
        return uuid;
    }

    protected boolean isExist(Object searchKey) {
        return storage.containsKey((String) searchKey);
    }
}
package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    protected void update(Resume resume, Object searchKey) {
        storage.put(resume.getUuid(), resume);
    }

    protected void save(Resume resume, Object searchKey) {
        storage.put(resume.getUuid(), resume);
    }

    protected Resume get(String uuid, Object searchKey) {
        return storage.get(uuid);
    }

    protected void delete(String uuid, Object searchKey) {
        storage.remove(uuid);
    }

    protected Object findIndex(String uuid) {
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (uuid.equals(entry.getKey())) {
                return uuid;
            }
        }
        return null;
    }

    protected boolean isExist(Object searchKey) {
        return Objects.equals(searchKey, null);
    }
}
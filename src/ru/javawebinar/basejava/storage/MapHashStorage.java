package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapHashStorage extends AbstractStorage {

    private final Map<Integer, Resume> storage = new HashMap<>();

    public void clear() {
        storage.clear();
    }

    public int size() {
        return storage.size();
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage.put(resume.hashCode(), resume);
        doDelete(searchKey);
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        storage.put(resume.hashCode(), resume);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get((Integer) searchKey);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove((Integer) searchKey);
    }

    @Override
    protected List<Resume> doGetAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected Object findSearchKey(String uuid) {
        for (Map.Entry<Integer, Resume> entry : storage.entrySet()) {
            if (uuid.equals(entry.getValue().getUuid())) {
                return entry.getValue().hashCode();
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return storage.containsKey((Integer) searchKey);
    }
}
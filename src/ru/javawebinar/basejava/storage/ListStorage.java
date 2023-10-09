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

    protected void doUpdate(Resume resume, Object searchKey) {
        storage.set((int) searchKey, resume);
    }

    protected void doSave(Resume resume, Object searchKey) {
        storage.add(resume);
    }

    protected Resume doGet(Object searchKey) {
        return storage.get((int) searchKey);
    }

    protected void doDelete(Object searchKey) {
        storage.remove((int) searchKey);
    }

    protected Object findSearchKey(String uuid) {
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
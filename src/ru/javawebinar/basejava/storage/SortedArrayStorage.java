package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object findSearchKey(String uuid) {
        Resume resume = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, countResume, resume);
    }

    @Override
    protected void saveResume(Resume resume, int searchKey) {
        searchKey = -(searchKey) - 1;
        System.arraycopy(storage, searchKey, storage, searchKey + 1, countResume - searchKey);
        storage[searchKey] = resume;
    }

    @Override
    protected void deleteResume(int searchKey) {
        System.arraycopy(storage, searchKey + 1, storage, searchKey, countResume - searchKey);
    }
}
package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getUuid);

    @Override
    protected Object findSearchKey(String uuid) {
        Resume resume = new Resume(uuid, "Name");
        return Arrays.binarySearch(storage, 0, countResume, resume, RESUME_COMPARATOR);
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
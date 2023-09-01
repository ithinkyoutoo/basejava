package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int findIndex(String uuid) {
        Resume resume = new Resume();
        resume.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, countResume, resume);
    }

    @Override
    protected void saveResume(Resume resume) {
        int index = findIndex(resume.getUuid());
        index = -(index) - 1;
        System.arraycopy(storage, index, storage, index + 1, countResume - index);
        storage[index] = resume;
        countResume++;
    }

    @Override
    protected void deleteResume(int index) {
        countResume--;
        if (index < countResume) {
            System.arraycopy(storage, index + 1, storage, index, countResume - index);
        }
        storage[countResume] = null;
    }
}
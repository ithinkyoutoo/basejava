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
    protected void sortResume() {
        for (int i = 1; i < countResume; i++) {
            Resume tempResume = storage[i];
            int index = Arrays.binarySearch(storage, 0, i, tempResume);
            if (index < 0) {
                index = -(index) - 1;
            }
            System.arraycopy(storage, index, storage, index + 1, i - index);
            storage[index] = tempResume;
        }
    }
}
package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object findSearchKey(String uuid) {
        for (int i = 0; i < countResume; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void saveResume(Resume resume, int index) {
        storage[countResume] = resume;
    }

    @Override
    protected void deleteResume(int index) {
        storage[index] = storage[countResume];
    }
}
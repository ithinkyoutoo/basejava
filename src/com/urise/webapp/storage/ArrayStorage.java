package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage implements Storage {

    private static final int CAPACITY = 10_000;

    private final Resume[] storage = new Resume[CAPACITY];
    private int countResume;

    public void clear() {
        Arrays.fill(storage, 0, countResume, null);
        countResume = 0;
    }

    public void update(Resume resume) {
        String uuid = resume.getUuid();
        int index = findIndex(uuid);
        if (index < 0) {
            printError(uuid);
        } else {
            storage[index] = resume;
        }
    }

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        if (countResume == CAPACITY) {
            System.out.println("Хранилище заполнено, вы не можете добавить новое резюме");
        } else if (findIndex(uuid) >= 0) {
            System.out.println("Резюме " + uuid + " уже существует");
        } else {
            storage[countResume++] = resume;
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            printError(uuid);
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            printError(uuid);
        } else {
            storage[index] = storage[--countResume];
            storage[countResume] = null;
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, countResume);
    }

    public int size() {
        return countResume;
    }

    private int findIndex(String uuid) {
        for (int i = 0; i < countResume; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    private void printError(String uuid) {
        System.out.println("Резюме " + uuid + " не найдено");
    }
}
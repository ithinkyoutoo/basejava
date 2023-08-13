package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage {

    private static final int CAPACITY = 10_000;

    private final Resume[] storage = new Resume[CAPACITY];
    private int countResume;

    public void clear() {
        Arrays.fill(storage, 0, countResume, null);
        countResume = 0;
    }

    public void update(Resume resume) {
        String uuid = resume.getUuid();
        if (hasResume(uuid)) {
            for (int i = 0; i < countResume; i++) {
                if (uuid.equals(storage[i].getUuid())) {
                    storage[i] = resume;
                }
            }
        } else {
            printError(uuid);
        }
    }

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        if (countResume == CAPACITY) {
            System.out.println("Хранилище заполнено, вы не можете добавить новое резюме");
        } else if (hasResume(uuid)) {
            System.out.println("Резюме " + uuid + " уже существует");
        } else {
            storage[countResume++] = resume;
        }
    }

    public Resume get(String uuid) {
        if (hasResume(uuid)) {
            for (int i = 0; i < countResume; i++) {
                if (uuid.equals(storage[i].getUuid())) {
                    return storage[i];
                }
            }
        }
        printError(uuid);
        return null;
    }

    public void delete(String uuid) {
        if (hasResume(uuid)) {
            for (int i = 0; i < countResume; i++) {
                if (uuid.equals(storage[i].getUuid())) {
                    countResume--;
                    System.arraycopy(storage, i + 1, storage, i, countResume - i);
                    storage[countResume] = null;
                }
            }
        } else {
            printError(uuid);
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, countResume);
    }

    public int size() {
        return countResume;
    }

    private boolean hasResume(String uuid) {
        for (Resume resume : getAll()) {
            if (uuid.equals(resume.getUuid())) {
                return true;
            }
        }
        return false;
    }

    private void printError(String uuid) {
        System.out.println("Резюме " + uuid + " не найдено");
    }
}
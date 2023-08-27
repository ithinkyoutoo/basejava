package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {

    protected static final int CAPACITY = 10_000;

    protected final Resume[] storage = new Resume[CAPACITY];
    protected int countResume;

    public final void clear() {
        Arrays.fill(storage, 0, countResume, null);
        countResume = 0;
    }

    public final void update(Resume resume) {
        String uuid = resume.getUuid();
        int index = findIndex(uuid);
        if (index < 0) {
            printError(uuid);
        } else {
            storage[index] = resume;
        }
    }

    public final void save(Resume resume) {
        String uuid = resume.getUuid();
        if (countResume == CAPACITY) {
            System.out.println("Хранилище заполнено, вы не можете добавить новое резюме");
        } else if (findIndex(uuid) >= 0) {
            System.out.println("Резюме " + uuid + " уже существует");
        } else {
            storage[countResume++] = resume;
            sortResume();
        }
    }

    public final Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            printError(uuid);
            return null;
        }
        return storage[index];
    }

    public final void delete(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            printError(uuid);
        } else {
            storage[index] = storage[--countResume];
            storage[countResume] = null;
            sortResume();
        }
    }

    public final Resume[] getAll() {
        return Arrays.copyOf(storage, countResume);
    }

    public final int size() {
        return countResume;
    }

    protected abstract int findIndex(String uuid);

    protected void sortResume() {
    }

    private void printError(String uuid) {
        System.out.println("Резюме " + uuid + " не найдено");
    }
}
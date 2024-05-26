package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private final File directory;
    private Serialization strategy;

    protected FileStorage(String dir, Serialization strategy) {
        Objects.requireNonNull(dir, "directory must not be null");
        Objects.requireNonNull(strategy, "Serialization strategy must not be null");
        this.directory = new File(dir);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(dir + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(dir + " is not readable/writable");
        }
        this.strategy = strategy;
    }

    public void setStrategy(Serialization strategy) {
        this.strategy = strategy;
    }

    @Override
    public void clear() {
        for (File file : getFiles(directory)) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        return getFiles(directory).length;
    }

    @Override
    protected void doUpdate(Resume resume, File file) {
        try {
            strategy.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File write error", file.getName(), e);
        }
    }

    @Override
    protected void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + file.getAbsolutePath(), file.getName(), e);
        }
        doUpdate(resume, file);
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return strategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File read error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        File[] files = getFiles(directory);
        List<Resume> resume = new ArrayList<>(files.length);
        for (File file : files) {
            resume.add(doGet(file));
        }
        return resume;
    }

    @Override
    protected File findSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    private File[] getFiles(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            return files;
        }
        throw new StorageException(directory + " directory read error");
    }
}
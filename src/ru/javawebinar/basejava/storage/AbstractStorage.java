package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    private static final Comparator<Resume> RESUME_COMPARATOR
            = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    public final void update(Resume resume) {
        LOG.info("Update " + resume);
        doUpdate(resume, getExistingSearchKey(resume.getUuid()));
    }

    public final void save(Resume resume) {
        LOG.info("Save " + resume);
        doSave(resume, getNotExistingSearchKey(resume.getUuid()));
    }

    public final Resume get(String uuid) {
        LOG.info("Get Resume " + uuid);
        return doGet(getExistingSearchKey(uuid));
    }

    public final void delete(String uuid) {
        LOG.info("Delete Resume " + uuid);
        doDelete(getExistingSearchKey(uuid));
    }

    public final List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> resume = doGetAll();
        resume.sort(RESUME_COMPARATOR);
        return resume;
    }

    protected abstract void doUpdate(Resume resume, SK searchKey);

    protected abstract void doSave(Resume resume, SK searchKey);

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract List<Resume> doGetAll();

    protected abstract SK findSearchKey(String uuid);

    protected abstract boolean isExist(SK searchKey);

    private SK getExistingSearchKey(String uuid) {
        SK searchKey = findSearchKey(uuid);
        if (isExist(searchKey)) {
            return searchKey;
        }
        LOG.warning("Resume " + uuid + " not exist");
        throw new NotExistStorageException(uuid);
    }

    private SK getNotExistingSearchKey(String uuid) {
        SK searchKey = findSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {

    private final Resume[] storage = new Resume[10000];
    private int countResume;

    public void clear() {
        Arrays.fill(storage, 0, countResume, null);
        countResume = 0;
    }

    public void save(Resume r) {
        storage[countResume++] = r;
    }

    public Resume get(String uuid) {
        for (int i = 0; i < countResume; i++) {
            if (uuid.equals(storage[i].uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    public void delete(String uuid) {
        for (int i = 0; i < countResume; i++) {
            if (uuid.equals(storage[i].uuid)) {
                countResume--;
                System.arraycopy(storage, i + 1, storage, i, countResume - i);
                storage[countResume] = null;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, countResume);
    }

    public int size() {
        return countResume;
    }
}
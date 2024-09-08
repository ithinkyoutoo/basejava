package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String PROPS = "D:\\java\\basejava\\config\\resumes.properties";
    private static final Config INSTANCE = new Config();

    private final File storageDir;
    private final Storage sqlStorage;

    private Config() {
        try (InputStream is = new FileInputStream(PROPS)) {
            Properties props = new Properties();
            props.load(is);
            storageDir = checkDir(new File(props.getProperty("storage.dir")));
            sqlStorage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"),
                    props.getProperty("db.password"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS);
        }
    }

    public static File getStorageDir() {
        return INSTANCE.storageDir;
    }

    public static Storage getSqlStorage() {
        return INSTANCE.sqlStorage;
    }

    private File checkDir(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + " is not directory");
        }
        if (!dir.canRead() || !dir.canWrite()) {
            throw new IllegalArgumentException(dir + " is not readable/writable");
        }
        return dir;
    }
}

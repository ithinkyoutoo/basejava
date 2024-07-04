package ru.javawebinar.basejava;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String PROPS = "config\\resumes.properties";
    private static final Config INSTANCE = new Config();

    private final String storageDir;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private Config() {
        try (InputStream is = new FileInputStream(PROPS)) {
            Properties props = new Properties();
            props.load(is);
            storageDir = props.getProperty("storage.dir");
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS);
        }
    }

    public static String getStorageDir() {
        return INSTANCE.storageDir;
    }

    public static String getDbUrl() {
        return INSTANCE.dbUrl;
    }

    public static String getDbUser() {
        return INSTANCE.dbUser;
    }

    public static String getDbPassword() {
        return INSTANCE.dbPassword;
    }
}

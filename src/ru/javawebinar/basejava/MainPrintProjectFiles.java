package ru.javawebinar.basejava;

import java.io.File;
import java.util.Objects;

public class MainPrintProjectFiles {

    public static void main(String[] args) {
        File dir = new File("D:\\java\\basejava");

        print(dir);
    }

    private static void print(File dir) {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                print(file);
            } else {
                System.out.println(file.getAbsolutePath());
            }
        }
    }
}

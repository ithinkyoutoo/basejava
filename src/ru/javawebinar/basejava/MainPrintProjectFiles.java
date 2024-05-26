package ru.javawebinar.basejava;

import java.io.File;
import java.util.Objects;

public class MainPrintProjectFiles {

    private static final int INDENT = 2;

    public static void main(String[] args) {
        File dir = new File("D:\\java\\basejava\\src");

        printTree(dir);
    }

    private static void printTree(File dir) {
        int rootNameCount = getNameCount(dir);
        printTree(dir, rootNameCount);
    }

    private static void printTree(File dir, int rootNameCount) {
        String str = " ";
        int count = getNameCount(dir) - rootNameCount;
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            System.out.println(str.repeat(count * INDENT) + file.getName());
            if (file.isDirectory()) {
                printTree(file, rootNameCount);
            }
        }
    }

    private static int getNameCount(File dir) {
        return dir.toPath().getNameCount();
    }
}

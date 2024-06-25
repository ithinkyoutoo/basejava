package ru.javawebinar.basejava.util;

import java.io.IOException;

@FunctionalInterface
public interface CustomRunnable {

    void run() throws IOException;
}

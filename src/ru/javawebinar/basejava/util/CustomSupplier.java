package ru.javawebinar.basejava.util;

import java.io.IOException;

@FunctionalInterface
public interface CustomSupplier<T> {

    T get() throws IOException;
}

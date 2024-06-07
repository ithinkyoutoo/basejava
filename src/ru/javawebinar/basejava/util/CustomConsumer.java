package ru.javawebinar.basejava.util;

import java.io.IOException;

@FunctionalInterface
public interface CustomConsumer<T> {

    void accept(T t) throws IOException;
}

package ru.javawebinar.basejava.util;

import java.io.IOException;

@FunctionalInterface
public interface BiCustomConsumer<K, V> {

    void accept(K k, V v) throws IOException;
}

package ru.javawebinar.basejava.util;

import java.util.List;

public class HtmlUtil {

    public static String getString(List<String> items) {
        return items == null ? "" : items.stream().reduce("", (str, item) -> str + item + "\n");
    }
}

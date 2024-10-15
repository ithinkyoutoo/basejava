package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public class HtmlUtil {

    public static String getLink(String str, Resume r) {
        ContactType type = ContactType.valueOf(str);
        return type.toHtml(r.getContact(type));
    }

    public static String getString(List<String> items) {
        return items == null ? "" : items.stream().reduce("", (str, item) -> str + item + "\n");
    }
}

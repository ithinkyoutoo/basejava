package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public class HtmlUtil {

    public static String getContactLink(String contact, Resume r) {
        ContactType type = ContactType.valueOf(contact);
        return type.toHtml(r.getContact(type));
    }

    public static String getCompanyLink(String website) {
        return website.isEmpty() ? "#no_scroll" : "https://" + website;
    }

    public static String getString(List<String> items) {
        return items == null ? "" : items.stream().reduce("", (str, item) -> str + item + "\n");
    }
}

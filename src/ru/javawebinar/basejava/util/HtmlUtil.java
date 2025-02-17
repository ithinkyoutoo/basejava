package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.Company;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public class HtmlUtil {

    public static String getContactLink(String contact, Resume r) {
        ContactType type = ContactType.valueOf(contact);
        return type.toHtml(r.getContact(type));
    }

    public static String getCompanyLink(String website) {
        return website.isEmpty() ? "javascript:void(0)" : "https://" + website;
    }

    public static String getString(List<String> items) {
        return items == null ? "" : String.join("\n", items);
    }

    public static String formatDates(Company.Period period) {
        return DateUtil.getFullString(period.getBeginDate()) + " - " + DateUtil.getFullString(period.getEndDate());
    }
}

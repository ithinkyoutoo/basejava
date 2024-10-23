package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {

    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String getString(LocalDate date) {
        if (date == null) {
            return "";
        }
        if (DateUtil.NOW.equals(date)) {
            return "н. в.";
        }
        return date.getMonthValue() + "/" + date.getYear();
    }

    public static String getFullString(LocalDate date) {
        if (DateUtil.NOW.equals(date)) {
            return "н. в.";
        }
        String month = date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
        StringBuilder monthYear = new StringBuilder(month);
        monthYear.setCharAt(0, Character.toUpperCase(monthYear.charAt(0)));
        return monthYear.append(" ").append(date.getYear()).toString();
    }
}

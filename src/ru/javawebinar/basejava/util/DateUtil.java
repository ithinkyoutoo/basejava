package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {

    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate parse(String str) {
        if (str.isEmpty() || "н. в.".equals(str)) {
            return NOW;
        }
        YearMonth date = YearMonth.parse(str, FORMATTER);
        return LocalDate.of(date.getYear(), date.getMonth(), 1);
    }

    public static String format(LocalDate date) {
        if (date == null) {
            return "";
        }
        return NOW.equals(date) ? "н. в." : date.format(FORMATTER);
    }

    public static String getFullString(LocalDate date) {
        if (NOW.equals(date)) {
            return "н. в.";
        }
        String month = date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
        StringBuilder monthYear = new StringBuilder(month);
        monthYear.setCharAt(0, Character.toUpperCase(monthYear.charAt(0)));
        return monthYear.append(" ").append(date.getYear()).toString();
    }
}

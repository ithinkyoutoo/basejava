package ru.javawebinar.basejava.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;


public class XmlLocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public String marshal(LocalDate ld) {
        return ld.toString();
    }

    @Override
    public LocalDate unmarshal(String str) {
        return LocalDate.parse(str);
    }
}

package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

import java.util.UUID;

import static ru.javawebinar.basejava.model.ResumeTestData.newResume;

public class TestData {

    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_4 = UUID.randomUUID().toString();
    public static final String FULL_NAME_1 = "name1";
    public static final String FULL_NAME_2 = "name2";
    public static final String FULL_NAME_3 = "name3";
    public static final String FULL_NAME_4 = "name4";
    public static final Resume RESUME_1 = newResume(UUID_1, FULL_NAME_1);
    public static final Resume RESUME_2 = newResume(UUID_2, FULL_NAME_2);
    public static final Resume RESUME_3 = newResume(UUID_3, FULL_NAME_3);
    public static final Resume RESUME_4 = newResume(UUID_4, FULL_NAME_4);
}

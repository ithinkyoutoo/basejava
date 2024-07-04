package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.DateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.time.Month.*;
import static ru.javawebinar.basejava.model.ContactType.*;
import static ru.javawebinar.basejava.model.SectionType.*;

public class ResumeTestData {

    public static void main(String[] args) {
        printResume(newResume("uuid", "Григорий Кислин"));
    }

    public static Resume newResume(String uuid, String fullName) {
        Resume r = new Resume(uuid, fullName);
//        setContacts(r);
//        setTextSections(r);
//        setListSections(r);
//        setCompanySections(r);
        return r;
    }

    private static void setContacts(Resume r) {
        r.setContact(TEL, "+7(921) 855-0482");
        r.setContact(SKYPE, "grigory.kislin");
        r.setContact(EMAIL, "gkislin@yandex.ru");
        r.setContact(LINKEDIN, "www.linkedin.com/in/gkislin");
        r.setContact(GITHUB, "www.github.com/gkislin");
        r.setContact(STACKOVERFLOW, "www.stackoverflow.com/users/548473/grigory-kislin");
        r.setContact(HOME_PAGE, "www.gkislin.ru");
    }

    private static void setTextSections(Resume r) {
        r.setSection(OBJECTIVE, new TextSection(
                "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        r.setSection(PERSONAL, new TextSection(
                "Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
    }

    private static void setListSections(Resume r) {
        String text = """
                Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения \s
                автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на \s
                Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для \s
                комплексных DIY смет.
                С 2013 года: разработка проектов "Разработка Web приложения","Java Enterprise", "Многомодульный \s
                maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие \s
                (JMS/AKKA)". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.
                Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. \s
                Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.
                """;
        r.setSection(ACHIEVEMENT, new ListSection(text));
        text = """
                JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2
                Version control: Subversion, Git, Mercury, ClearCase, Perforce
                DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL,\s
                HSQLDB
                """;
        r.setSection(QUALIFICATIONS, new ListSection(text));
    }

    private static void setCompanySections(Resume r) {
        r.setSection(EXPERIENCE, new CompanySection(getCompany1(), getCompany2()));
        r.setSection(EDUCATION, new CompanySection(getCompany3(), getCompany4(), getCompany5()));
    }

    private static Company getCompany1() {
        String name = "Java Online Projects";
        String website = "www.javaops.ru";
        LocalDate begin = DateUtil.of(2013, OCTOBER);
        String title = "Автор проекта";
        String description = "Создание, организация и проведение Java онлайн проектов и стажировок.";
        Company.Period period = new Company.Period(begin, title, description);
        return new Company(name, website, period);
    }

    private static Company getCompany2() {
        String name = "Wrike";
        String website = "www.wrike.co";
        LocalDate begin = DateUtil.of(2014, OCTOBER);
        LocalDate end = DateUtil.of(2016, JANUARY);
        String title = "Старший разработчик (backend)";
        String description = """
                Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring,\s
                MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1,\s
                OAuth2, JWT SSO.
                """;
        Company.Period period = new Company.Period(begin, end, title, description);
        return new Company(name, website, period);
    }

    private static Company getCompany3() {
        String name = "Coursera";
        String website = "www.coursera.org/learn/progfun1";
        LocalDate begin = DateUtil.of(2013, MARCH);
        LocalDate end = DateUtil.of(2013, MAY);
        String title = "'Functional Programming Principles in Scala' by Martin Odersky";
        Company.Period period = new Company.Period(begin, end, title);
        return new Company(name, website, period);
    }

    private static Company getCompany4() {
        String name = "Luxoft";
//        String website = "www.luxoft-training.ru/training/catalog/course.html?ID=22366";
        LocalDate begin = DateUtil.of(2011, MARCH);
        LocalDate end = DateUtil.of(2011, APRIL);
        String title = "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'";
        Company.Period period = new Company.Period(begin, end, title);
        return new Company(name, period);
    }

    private static Company getCompany5() {
        String name = "Санкт-Петербургский национальный исследовательский университет информационных технологий," +
                "механики и оптики";
        String website = "www.itmo.ru";
        LocalDate begin = DateUtil.of(1993, SEPTEMBER);
        LocalDate end = DateUtil.of(1996, JULY);
        String title = "Аспирантура (программист С, С++)";
        Company.Period period1 = new Company.Period(begin, end, title);
        begin = DateUtil.of(1987, SEPTEMBER);
        end = DateUtil.of(1993, JULY);
        title = "Инженер (программист Fortran, C)";
        Company.Period period2 = new Company.Period(begin, end, title);
        return new Company(name, website, period1, period2);
    }

    private static void printResume(Resume r) {
        System.out.println("\n" + r.getFullName() + "\n");
        printContacts(r);
        printSections(r);
    }

    private static void printContacts(Resume r) {
        for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
            System.out.println(entry.getKey().getTitle() + " " + entry.getValue());
        }
    }

    private static void printSections(Resume r) {
        for (Map.Entry<SectionType, Section> entry : r.getSections().entrySet()) {
            switch (entry.getKey()) {
                case OBJECTIVE, PERSONAL -> System.out.println("\n" + entry.getKey() + "\n"
                        + entry.getValue().toString());
                case ACHIEVEMENT -> printListSection(entry, ACHIEVEMENT);
                case QUALIFICATIONS -> printListSection(entry, QUALIFICATIONS);
                case EXPERIENCE -> printCompanySection(entry, EXPERIENCE);
                case EDUCATION -> printCompanySection(entry, EDUCATION);
            }
        }
    }

    private static void printListSection(Map.Entry<SectionType, Section> entry, SectionType type) {
        System.out.println("\n" + type);
        ListSection list = (ListSection) entry.getValue();
        list.getItems().forEach(System.out::println);
    }

    private static void printCompanySection(Map.Entry<SectionType, Section> entry, SectionType type) {
        System.out.println("\n" + type);
        CompanySection list = (CompanySection) entry.getValue();
        for (Company c : list.getCompanies()) {
            String website = c.getWebsite();
            System.out.println("\n" + c.getName() + " " + (website != null ? website : ""));
            for (Company.Period p : c.getPeriods()) {
                LocalDate endDate = p.getEndDate();
                System.out.println(p.getBeginDate() + " - " + ((endDate.equals(DateUtil.NOW)) ? "Сейчас" : endDate));
                System.out.println(p.getTitle());
                List<String> description = p.getDescription();
                if (description != null) {
                    description.forEach(System.out::println);
                }
            }
        }
    }
}
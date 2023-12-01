package ru.javawebinar.basejava.model;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.basejava.model.ContactType.*;
import static ru.javawebinar.basejava.model.SectionType.*;

public class ResumeTestData {

    public static void main(String[] args) {
        Resume r = new Resume("Григорий Кислин");

        setContacts(r);
        setTextSections(r);
        setListSections(r);
        setCompanySections(r);
        printResume(r);
    }

    private static void setContacts(Resume r) {
        r.getContacts().put(TEL, "+7(921) 855-0482");
        r.getContacts().put(SKYPE, "grigory.kislin");
        r.getContacts().put(EMAIL, "gkislin@yandex.ru");
        r.getContacts().put(LINKEDIN, "www.linkedin.com/in/gkislin");
        r.getContacts().put(GITHUB, "www.github.com/gkislin");
        r.getContacts().put(STACKOVERFLOW, "www.stackoverflow.com/users/548473/grigory-kislin");
        r.getContacts().put(HOMEPAGE, "www.gkislin.ru");
    }

    private static void setTextSections(Resume r) {
        r.getSections().put(OBJECTIVE, new TextSection(
                "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        r.getSections().put(PERSONAL, new TextSection(
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
        r.getSections().put(ACHIEVEMENT, new ListSection(List.of(split(text))));
        text = """
                JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2
                Version control: Subversion, Git, Mercury, ClearCase, Perforce
                DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL,\s
                HSQLDB
                """;
        r.getSections().put(QUALIFICATIONS, new ListSection(List.of(split(text))));
    }

    private static String[] split(String text) {
        return text.split("\\n");
    }

    private static void setCompanySections(Resume r) {
        r.getSections().put(EXPERIENCE, new CompanySection(getCompanySection(getCompany1(), getCompany2())));
        r.getSections().put(EDUCATION, new CompanySection(getCompanySection(getCompany3(), getCompany4())));
    }

    private static List<Company> getCompanySection(Company company1, Company company2) {
        List<Company> companies = new ArrayList<>();
        companies.add(company1);
        companies.add(company2);
        return companies;
    }

    private static Company getCompany1() {
        Company company = new Company();
        company.setName("Java Online Projects");
        company.setWebsite("www.javaops.ru");
        Company.Period period = new Company.Period();
        period.setBeginDate(YearMonth.of(2013, Month.JANUARY));
        period.setEndDate(null);
        period.setTitle("Автор проекта");
        period.setDescription("Создание, организация и проведение Java онлайн проектов и стажировок.");
        List<Company.Period> periods = new ArrayList<>();
        periods.add(period);
        company.setPeriods(periods);
        return company;
    }

    private static Company getCompany2() {
        Company company = new Company();
        company.setName("Wrike");
        company.setWebsite("www.wrike.co");
        Company.Period period = new Company.Period();
        period.setBeginDate(YearMonth.of(2014, Month.OCTOBER));
        period.setEndDate(YearMonth.of(2016, Month.JANUARY));
        period.setTitle("Старший разработчик (backend)");
        String description = """
                Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring,\s
                MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1,\s
                OAuth2, JWT SSO.
                """;
        period.setDescription(description);
        List<Company.Period> periods = new ArrayList<>();
        periods.add(period);
        company.setPeriods(periods);
        return company;
    }

    private static Company getCompany3() {
        Company company = new Company();
        company.setName("Coursera");
        company.setWebsite("www.coursera.org/learn/progfun1");
        Company.Period period = new Company.Period();
        period.setBeginDate(YearMonth.of(2013, Month.MARCH));
        period.setEndDate(YearMonth.of(2013, Month.MAY));
        period.setTitle("'Functional Programming Principles in Scala' by Martin Odersky");
        List<Company.Period> periods = new ArrayList<>();
        periods.add(period);
        company.setPeriods(periods);
        return company;
    }

    private static Company getCompany4() {
        Company company = new Company();
        company.setName("Luxoft");
        company.setWebsite("www.luxoft-training.ru/training/catalog/course.html?ID=22366");
        Company.Period period = new Company.Period();
        period.setBeginDate(YearMonth.of(2011, Month.MARCH));
        period.setEndDate(YearMonth.of(2011, Month.APRIL));
        period.setTitle("Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'");
        List<Company.Period> periods = new ArrayList<>();
        periods.add(period);
        company.setPeriods(periods);
        return company;
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
        for (String s : list.getList()) {
            System.out.println(s);
        }
    }

    private static void printCompanySection(Map.Entry<SectionType, Section> entry, SectionType type) {
        System.out.println("\n" + type);
        CompanySection list = (CompanySection) entry.getValue();
        for (Company c : list.getCompanies()) {
            System.out.println("\n" + c.getName() + " " + c.getWebsite());
            for (Company.Period p : c.getPeriods()) {
                System.out.println(p.getBeginDate() + " - " + (p.getEndDate() != null ? p.getEndDate() : "Сейчас"));
                System.out.println(p.getTitle());
                if (p.getDescription() != null) {
                    System.out.println(p.getDescription());
                }
            }
        }
    }
}
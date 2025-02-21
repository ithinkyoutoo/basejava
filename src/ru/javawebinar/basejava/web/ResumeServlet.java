package ru.javawebinar.basejava.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.getSqlStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "add" -> r = Resume.EMPTY;
            case "view", "edit" -> r = storage.get(uuid);
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = replaceSpaces(request.getParameter("fullName"), " ");
        boolean isNewResume = uuid.isEmpty();
        Resume r = isNewResume ? new Resume(fullName) : storage.get(uuid);
        r.setFullName(fullName);
        editContacts(r, request);
        editSections(r, request);
        if (isNewResume) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }

    private void editContacts(Resume r, HttpServletRequest request) {
        for (ContactType type : ContactType.values()) {
            String value = replaceSpaces(request.getParameter(type.name()), "");
            if (value.isEmpty()) {
                r.getContacts().remove(type);
            } else {
                r.setContact(type, value);
            }
        }
    }

    private void editSections(Resume r, HttpServletRequest request) {
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value.isBlank()) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE, PERSONAL -> r.setSection(type, new TextSection(replaceSpaces(value, " ")));
                    case ACHIEVEMENT, QUALIFICATIONS -> r.setSection(type, new ListSection(getList(value)));
                    case EXPERIENCE, EDUCATION -> {
                        List<Company> companies = getCompanies(request, type);
                        if (companies.isEmpty()) {
                            r.getSections().remove(type);
                        } else {
                            r.setSection(type, new CompanySection(companies));
                        }
                    }
                }
            }
        }
    }

    private List<Company> getCompanies(HttpServletRequest request, SectionType type) {
        List<Company> companies = new ArrayList<>();
        int size = Integer.parseInt(request.getParameter(type + "companySize"));
        for (int i = 1; i <= size; i++) {
            String[] values = request.getParameterValues(type + "company" + i);
            String name = replaceSpaces(values[0], " ");
            if (!name.isEmpty()) {
                String website = replaceSpaces(values[1], "");
                companies.add(new Company(name, website, getPeriods(request, type, i)));
            }
        }
        return companies;
    }

    private List<Company.Period> getPeriods(HttpServletRequest request, SectionType type, int companyNum) {
        List<Company.Period> periods = new ArrayList<>();
        int size = Integer.parseInt(request.getParameter(type + "company" + companyNum + "periodSize"));
        for (int i = 1; i <= size; i++) {
            String[] values = request.getParameterValues(type + "company" + companyNum + "period" + i);
            String title = replaceSpaces(values[2], " ");
            if (!title.isEmpty()) {
                LocalDate begin = DateUtil.parse(values[0]);
                LocalDate end = DateUtil.parse(values[1]);
                List<String> description = getList(values[3]);
                periods.add(new Company.Period(begin, end, title, description));
            }
        }
        return periods;
    }

    private String replaceSpaces(String str, String space) {
        return str.strip().replaceAll("\\s+", space);
    }

    private List<String> getList(String str) {
        return str.lines()
                .map(s -> replaceSpaces(s, " "))
                .filter(s -> !s.isEmpty())
                .toList();
    }
}

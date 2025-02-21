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
            case "view" -> r = storage.get(uuid);
            case "edit" -> r = addEmpty(storage.get(uuid));
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

    private Resume addEmpty(Resume r) {
        for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
            CompanySection section = (CompanySection) r.getSection(type);
            List<Company> emptyFirstCompany = new ArrayList<>();
            emptyFirstCompany.add(Company.EMPTY);
            if (section != null) {
                for (Company company : section.getCompanies()) {
                    List<Company.Period> emptyFirstPeriod = new ArrayList<>();
                    emptyFirstPeriod.add(Company.Period.EMPTY);
                    emptyFirstPeriod.addAll(company.getPeriods());
                    emptyFirstCompany.add(new Company(company.getName(), company.getWebsite(), emptyFirstPeriod));
                }
            }
            r.setSection(type, new CompanySection(emptyFirstCompany));
        }
        return r;
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
            String typeName = type.name();
            String value = request.getParameter(typeName);
            String[] names = request.getParameterValues(typeName);
            if (value.isBlank() && isEmpty(names)) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE, PERSONAL -> r.setSection(type, new TextSection(replaceSpaces(value, " ")));
                    case ACHIEVEMENT, QUALIFICATIONS -> r.setSection(type, new ListSection(getList(value)));
                    case EXPERIENCE, EDUCATION -> r.setSection(type, newCompanySection(typeName, names, request));
                }
            }
        }
    }

    private CompanySection newCompanySection(String typeName, String[] names, HttpServletRequest request) {
        List<Company> companies = new ArrayList<>();
        String[] websites = request.getParameterValues(typeName + "website");
        for (int i = 0; i < names.length; i++) {
            String name = replaceSpaces(names[i], " ");
            if (!name.isEmpty()) {
                String website = replaceSpaces(websites[i], "");
                List<Company.Period> periods = new ArrayList<>();
                String pfx = typeName + i;
                String[] beginDates = request.getParameterValues(pfx + "beginDate");
                String[] endDates = request.getParameterValues(pfx + "endDate");
                String[] titles = request.getParameterValues(pfx + "title");
                String[] descriptions = request.getParameterValues(pfx + "description");
                for (int j = 0; j < titles.length; j++) {
                    String title = replaceSpaces(titles[j], " ");
                    if (!title.isEmpty()) {
                        LocalDate begin = DateUtil.parse(beginDates[j]);
                        LocalDate end = DateUtil.parse(endDates[j]);
                        List<String> description = getList(descriptions[j]);
                        periods.add(new Company.Period(begin, end, title, description));
                    }
                }
                companies.add(new Company(name, website, periods));
            }
        }
        return new CompanySection(companies);
    }

    private String replaceSpaces(String str, String space) {
        return str.strip().replaceAll("\\s+", space);
    }

    private boolean isEmpty(String[] names) {
        for (String name : names) {
            if (!name.isBlank()) {
                return false;
            }
        }
        return true;
    }

    private List<String> getList(String str) {
        return str.lines()
                .map(s -> replaceSpaces(s, " "))
                .filter(s -> !s.isEmpty())
                .toList();
    }
}

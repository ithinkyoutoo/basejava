package ru.javawebinar.basejava.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;

import java.io.IOException;
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
            case "view", "edit" -> r = uuid != null ? storage.get(uuid) : new Resume("", "");
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
        String fullName = removeSpace(request.getParameter("fullName"));
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

    private String removeSpace(String str) {
        return str.strip().replaceAll("\\s+", " ");
    }

    private void editContacts(Resume r, HttpServletRequest request) {
        for (ContactType type : ContactType.values()) {
            String value = removeSpace(request.getParameter(type.name()));
            if (!value.isEmpty()) {
                r.setContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
    }

    private void editSections(Resume r, HttpServletRequest request) {
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                r.setSection(type, switch (type) {
                    case OBJECTIVE, PERSONAL -> new TextSection(removeSpace(value));
                    case ACHIEVEMENT, QUALIFICATIONS -> new ListSection(getList(value));
                    case EXPERIENCE, EDUCATION -> null;
                });
            } else {
                r.getSections().remove(type);
            }
        }
    }

    private List<String> getList(String str) {
        return str.lines()
                .map(this::removeSpace)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}

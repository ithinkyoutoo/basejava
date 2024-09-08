package ru.javawebinar.basejava.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        Writer out = response.getWriter();
        out.write("<html><head><style>" +
                "table, tr {border: 1px solid black; border-collapse: collapse; width: 35%; height: 35px}" +
                "tr: nth-child(odd) {background-color: #c2e3ff}" +
                "th {text-align: center; background-color: #6dade1}" +
                "td {text-align: center}" +
                "</style></head>" +
                "<body><table>" +
                "<tr><th>uuid</th><th style=\"width: 37%\">full_name</th></tr>");
        Storage storage = Config.getSqlStorage();
        String uuid = request.getParameter("uuid");
        List<Resume> resumes = uuid == null ? storage.getAllSorted() : List.of(storage.get(uuid));
        for (Resume r : resumes) {
            out.write("<tr><td>" + r.getUuid() + "</td><td>" + r.getFullName() + "</td></tr>");
        }
        out.write("</table></body></html>");
    }
}

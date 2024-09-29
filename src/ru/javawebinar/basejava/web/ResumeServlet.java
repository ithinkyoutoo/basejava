package ru.javawebinar.basejava.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.storage.Storage;

import java.io.IOException;
import java.io.Writer;

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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html; charset=UTF-8");
        Writer out = response.getWriter();
        out.write("<html><head>" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" +
                "<link rel=\"stylesheet\" href=\"css/style.css\">" +
                "<title>Список всех резюме</title></head>" +
                "<body><section><table>" +
                "<tr><th>full_name</th><th>e-mail</th></tr>");
        for (Resume r : storage.getAllSorted()) {
            out.write("<tr><td><a href=\"resume?uuid=" + r.getUuid() + "\">" + r.getFullName() + "</a></td>" +
                    "<td>" + r.getContact(ContactType.EMAIL) + "</td></tr>");
        }
        out.write("</table></section></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}

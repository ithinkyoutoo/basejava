<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<section>
    <table>
        <tr>
            <th>full_name</th>
            <th>e-mail</th>
        </tr>
        <%
            for (Resume r : (List<Resume>) request.getAttribute("resumes")) {
        %>
        <tr>
            <td><a href="resume?uuid=<%=r.getUuid()%>"><%=r.getFullName()%></a></td>
            <td><%=r.getContact(ContactType.EMAIL)%></td>
        </tr>
        <%
            }
        %>
    </table>
</section>
</body>
</html>

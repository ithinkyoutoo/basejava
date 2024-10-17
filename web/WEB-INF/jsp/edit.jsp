<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.HtmlUtil" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section class="edit">
    <form method="post" action="resume" name="form" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=45 value="${resume.fullName}" required pattern=".*\S.*"></dd>
        </dl>
        <h3 class="edit">Контакты:</h3>
        <c:forEach var="type" items="${ContactType.values()}">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=45 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3 class="edit">Информация:</h3>
        <c:forEach var="type" items="${SectionType.values()}">
            <c:set var="typeName" value="${type.name()}"/>
            <c:set var="section" value="${resume.getSection(type)}"/>
            <c:choose>
                <c:when test="${'OBJECTIVE'.equals(typeName) || 'PERSONAL'.equals(typeName)}">
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><input type="text" name="${typeName}" size=91 value="${section.text}"></dd>
                    </dl>
                </c:when>
                <c:when test="${'ACHIEVEMENT'.equals(typeName) || 'QUALIFICATIONS'.equals(typeName)}">
                    <c:set var="items" value="${HtmlUtil.getString(section.items)}"/>
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><textarea name="${typeName}" cols="85" rows="6">${items}</textarea></dd>
                    </dl>
                </c:when>
            </c:choose>
        </c:forEach>
        <hr class="edit"/>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
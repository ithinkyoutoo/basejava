<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.HtmlUtil" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/edit.css">
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя <span class="name">(обязательно)</span></dt>
            <dd><input type="text" name="fullName" size=45 value="${resume.fullName}" required pattern=".*\S.*"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="${ContactType.values()}">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type}" size=45 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Информация:</h3>
        <c:forEach var="type" items="${SectionType.values()}">
            <c:set var="title" value="${type.title}"/>
            <c:set var="section" value="${resume.getSection(type)}"/>
            <c:choose>
                <c:when test="${type == 'OBJECTIVE'}">
                    <dl>
                        <dt>${title}</dt>
                        <dd><input type="text" name="${type}" size=91 value="${section.text}"></dd>
                    </dl>
                </c:when>
                <c:when test="${type == 'PERSONAL'}">
                    <dl>
                        <dt>${title}</dt>
                        <dd><textarea name="${type}" cols="85" rows="4">${section.text}</textarea></dd>
                    </dl>
                </c:when>
                <c:when test="${type == 'ACHIEVEMENT' || type == 'QUALIFICATIONS'}">
                    <c:set var="items" value="${HtmlUtil.getString(section.items)}"/>
                    <dl>
                        <dt>${title}</dt>
                        <dd><textarea name="${type}" cols="85" rows="6">${items}</textarea></dd>
                    </dl>
                </c:when>
                <c:when test="${type == 'EXPERIENCE' || type == 'EDUCATION'}">
                    <h3>${title}:</h3>
                    <input type="hidden" name="${type}" value="${type}">
                    <c:set var="countCompany" value="0"/>
                    <c:set var="countPeriod" value="0"/>
                    <%@ include file="fragments/company.jsp" %>
                    <%@ include file="fragments/period.jsp" %>
                    <input type="hidden" name="${type}company${countCompany}periodSize" value="${countPeriod}">
                    <c:forEach var="company" items="${section.companies}">
                        <c:set var="countPeriod" value="0"/>
                        <%@ include file="fragments/company.jsp" %>
                        <%@ include file="fragments/period.jsp" %>
                        <c:forEach var="period" items="${company.periods}">
                            <%@ include file="fragments/period.jsp" %>
                        </c:forEach>
                        <input type="hidden" name="${type}company${countCompany}periodSize" value="${countPeriod}">
                    </c:forEach>
                    <input type="hidden" name="${type}companySize" value="${countCompany}">
                </c:when>
            </c:choose>
        </c:forEach>
        <hr/>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
<%@ page import="ru.javawebinar.basejava.util.HtmlUtil" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/view.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            ${contactEntry.key.toHtml(contactEntry.value)}<br/>
        </c:forEach>
    <p>
    <hr/>
</section>
<section>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <c:set var="type" value="${sectionEntry.key}"/>
            <c:set var="typeName" value="${type.name()}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <h3>${type.title}</h3>
            <c:choose>
                <c:when test="${'OBJECTIVE'.equals(typeName) || 'PERSONAL'.equals(typeName)}">
                    ${section.text}
                </c:when>
                <c:when test="${'ACHIEVEMENT'.equals(typeName) || 'QUALIFICATIONS'.equals(typeName)}">
                    <div class="list-section">${HtmlUtil.getString(section.items)}</div>
                </c:when>
                <c:when test="${'EXPERIENCE'.equals(typeName) || 'EDUCATION'.equals(typeName)}">
                    <c:forEach var="company" items="${section.companies}">
                        <div class="company-title">
                            <b><a href="${HtmlUtil.getCompanyLink(company.website)}">${company.name}</a></b>
                        </div>
                        <c:forEach var="period" items="${company.periods}">
                            <dl>
                                <dt>
                                    ${DateUtil.getFullString(period.beginDate)}
                                    - ${DateUtil.getFullString(period.endDate)}
                                </dt>
                                <dd>${period.title}</dd>
                                <dd class="description">${HtmlUtil.getString(period.description)}</dd>
                            </dl>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
    <p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

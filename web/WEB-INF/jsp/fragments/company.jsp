<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<dl class="company">
    <dt class="company">Организация</dt>
    <dd class="company">
        <input type="text" name="${typeName}company${countCompany = countCompany + 1}" size=91
               value="${company.name}">
    </dd>
    <dt class="company">Сайт</dt>
    <dd class="company">
        <input type="text" name="${typeName}company${countCompany}" size=91
               value="${company.website}">
    </dd>
</dl>

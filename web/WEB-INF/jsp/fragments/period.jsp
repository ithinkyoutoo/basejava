<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<dl class="period">
    <dt class="period">Период <span class="date">(мм/гггг)</span></dt>
    <dd class="period">
        <label>начало</label>
        <input type="text" name="${type}company${countCompany}period${countPeriod = countPeriod + 1}" size=10
               value="${DateUtil.format(period.beginDate)}" pattern="^(0[1-9]|1[0-2])\/(19|20)[0-9]{2}$">
    </dd>
    <dd class="period">
        <label>окончание</label>
        <input type="text" name="${type}company${countCompany}period${countPeriod}" size=10
               value="${DateUtil.format(period.endDate)}" pattern="^(0[1-9]|1[0-2])\/(19|20)[0-9]{2}$|(н. в.)">
    </dd>
    <br/>
    <dt class="period">Должность</dt>
    <dd class="period">
        <input type="text" name="${type}company${countCompany}period${countPeriod}" size=91
               value="${period.title}">
    </dd>
    <dt class="period">Описание</dt>
    <dd class="period">
        <textarea name="${type}company${countCompany}period${countPeriod}" cols="85"
                  rows="6">${HtmlUtil.getString(period.description)}</textarea>
    </dd>
</dl>
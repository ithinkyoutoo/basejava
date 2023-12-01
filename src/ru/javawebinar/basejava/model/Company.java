package ru.javawebinar.basejava.model;

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

public class Company {

    private String name;
    private String website;
    private List<Period> periods;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (!Objects.equals(name, company.name)) return false;
        if (!Objects.equals(website, company.website)) return false;
        return Objects.equals(periods, company.periods);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (periods != null ? periods.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", periods=" + periods +
                '}';
    }

    public static class Period {

        private YearMonth beginDate;
        private YearMonth endDate;
        private String title;
        private String description;

        public void setBeginDate(YearMonth beginDate) {
            this.beginDate = beginDate;
        }

        public YearMonth getBeginDate() {
            return beginDate;
        }

        public void setEndDate(YearMonth endDate) {
            this.endDate = endDate;
        }

        public YearMonth getEndDate() {
            return endDate;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Period period = (Period) o;

            if (!Objects.equals(beginDate, period.beginDate)) return false;
            if (!Objects.equals(endDate, period.endDate)) return false;
            if (!Objects.equals(title, period.title)) return false;
            return Objects.equals(description, period.description);
        }

        @Override
        public int hashCode() {
            int result = beginDate != null ? beginDate.hashCode() : 0;
            result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
            result = 31 * result + (title != null ? title.hashCode() : 0);
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Period{" +
                    "beginDate='" + beginDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
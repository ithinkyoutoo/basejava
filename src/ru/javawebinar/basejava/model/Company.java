package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Company {

    private String name;
    private String website;
    private List<Period> periods;

    public Company(String name, String website, List<Period> periods) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(periods, "periods must not be null");
        this.name = name;
        this.website = website;
        this.periods = periods;
    }

    public void setName(String name) {
        Objects.requireNonNull(name, "name must not be null");
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
        Objects.requireNonNull(periods, "periods must not be null");
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

        if (!name.equals(company.name)) return false;
        if (!Objects.equals(website, company.website)) return false;
        return periods.equals(company.periods);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + periods.hashCode();
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

        private LocalDate beginDate;
        private LocalDate endDate;
        private String title;
        private String description;

        public Period(LocalDate beginDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(beginDate, "beginDate must not be null");
            Objects.requireNonNull(endDate, "endDate must not be null");
            Objects.requireNonNull(title, "title must not be null");
            this.beginDate = beginDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
        }

        public void setBeginDate(LocalDate beginDate) {
            Objects.requireNonNull(beginDate, "beginDate must not be null");
            this.beginDate = beginDate;
        }

        public LocalDate getBeginDate() {
            return beginDate;
        }

        public void setEndDate(LocalDate endDate) {
            Objects.requireNonNull(endDate, "endDate must not be null");
            this.endDate = endDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setTitle(String title) {
            Objects.requireNonNull(title, "title must not be null");
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

            if (!beginDate.equals(period.beginDate)) return false;
            if (!endDate.equals(period.endDate)) return false;
            if (!title.equals(period.title)) return false;
            return Objects.equals(description, period.description);
        }

        @Override
        public int hashCode() {
            int result = beginDate.hashCode();
            result = 31 * result + endDate.hashCode();
            result = 31 * result + title.hashCode();
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
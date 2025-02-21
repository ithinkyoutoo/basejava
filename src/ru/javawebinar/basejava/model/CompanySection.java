package ru.javawebinar.basejava.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanySection extends Section {

    @Serial
    private static final long serialVersionUID = 1L;
    public static final CompanySection EMPTY = new CompanySection(Company.EMPTY);

    private List<Company> companies = new ArrayList<>();

    private CompanySection() {
    }

    public CompanySection(Company... companies) {
        this(new ArrayList<>(List.of(companies)));
    }

    public CompanySection(List<Company> companies) {
        Objects.requireNonNull(companies, "companies must not be null");
        this.companies = companies;
    }

    public void setCompanies(List<Company> companies) {
        Objects.requireNonNull(companies, "companies must not be null");
        this.companies = companies;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;

        return companies.equals(that.companies);
    }

    @Override
    public int hashCode() {
        return companies.hashCode();
    }

    @Override
    public String toString() {
        return "CompanySection{" +
                "companies=" + companies +
                '}';
    }
}
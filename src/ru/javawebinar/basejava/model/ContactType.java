package ru.javawebinar.basejava.model;

public enum ContactType {
    TEL("Тел."),
    EMAIL("Почта"){
        @Override
        public String toHtml0(String value) {
            return toLink("mailto:" + value, value);
        }
    },
    SKYPE("Skype") {
        @Override
        public String toHtml0(String value) {
            return toLink("skype:" + value, value);
        }
    },
    LINKEDIN("LinkedIn") {
        @Override
        public String toHtml0(String value) {
            return toLink("https://" + value);
        }
    },
    GITHUB("GitHub") {
        @Override
        public String toHtml0(String value) {
            return toLink("https://" + value);
        }
    },
    STACKOVERFLOW("Stackoverflow") {
        @Override
        public String toHtml0(String value) {
            return toLink("https://" + value);
        }
    },
    HOME_PAGE("Домашняя страница") {
        @Override
        public String toHtml0(String value) {
            return toLink("https://" + value);
        }
    };

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }

    protected String toLink(String href) {
        return toLink(href, title);
    }

    protected String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }
}
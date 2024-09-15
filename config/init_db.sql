CREATE TABLE resume (
    PRIMARY KEY (uuid),
    uuid      CHAR(36)  NOT NULL,
    full_name TEXT      NOT NULL
);

CREATE TABLE contact (
    PRIMARY KEY (id),
    id          SERIAL,
    c_type      TEXT      NOT NULL,
    c_value     TEXT      NOT NULL,
    resume_uuid CHAR(36)  NOT NULL
                REFERENCES resume (uuid)
                ON DELETE CASCADE
);

CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact (resume_uuid, c_type);

CREATE TABLE section (
    PRIMARY KEY (id),
    id          SERIAL,
    s_type      TEXT      NOT NULL,
    s_value     TEXT      NOT NULL,
    resume_uuid CHAR(36)  NOT NULL
                REFERENCES resume (uuid)
                ON DELETE CASCADE
);

CREATE UNIQUE INDEX section_uuid_type_index
    ON section (resume_uuid, s_type);

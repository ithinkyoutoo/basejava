package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataStreamStrategy implements Serialization {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            write(r.getUuid(), dos);
            write(r.getFullName(), dos);
            writeContacts(r.getContacts(), dos);
            writeSections(r.getSections(), dos);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = read(dis);
            String fullName = read(dis);
            Resume resume = new Resume(uuid, fullName);
            addContacts(resume, dis);
            addSections(resume, dis);
            return resume;
        }
    }

    private void writeContacts(Map<ContactType, String> contacts, DataOutputStream dos) {
        writeSize(contacts.size(), dos);
        contacts.forEach((type, text) -> {
            write(type.name(), dos);
            write(text, dos);
        });
    }

    private void writeSections(Map<SectionType, Section> sections, DataOutputStream dos) {
        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            SectionType type = entry.getKey();
            Section section = entry.getValue();
            switch (type) {
                case OBJECTIVE, PERSONAL -> write(type, (TextSection) section, dos);
                case ACHIEVEMENT, QUALIFICATIONS -> write(type, (ListSection) section, dos);
                case EXPERIENCE, EDUCATION -> write(type, (CompanySection) section, dos);
            }
        }
    }

    private void write(SectionType type, TextSection section, DataOutputStream dos) {
        write(type.name(), dos);
        write(section.getText(), dos);
    }

    private void write(SectionType type, ListSection section, DataOutputStream dos) {
        write(type.name(), dos);
        writeItems(section.getItems(), dos);
    }

    private void write(SectionType type, CompanySection section, DataOutputStream dos) {
        write(type.name(), dos);
        writeCompanies(section.getCompanies(), dos);
    }

    private void writeCompanies(List<Company> companies, DataOutputStream dos) {
        writeSize(companies.size(), dos);
        companies.forEach(x -> {
            write(x.getName(), dos);
            write(x.getWebsite(), dos);
            writePeriods(x.getPeriods(), dos);
        });
    }

    private void writePeriods(List<Company.Period> periods, DataOutputStream dos) {
        writeSize(periods.size(), dos);
        periods.forEach(p -> {
            write(p.getBeginDate().toString(), dos);
            write(p.getEndDate().toString(), dos);
            write(p.getTitle(), dos);
            writeItems(p.getDescription(), dos);
        });
    }

    private void addContacts(Resume resume, DataInputStream dis) {
        Arrays.stream(new int[readSize(dis)])
                .forEach(x -> {
                    ContactType type = ContactType.valueOf(read(dis));
                    String text = read(dis);
                    resume.setContact(type, text);
                });
    }

    private void addSections(Resume resume, DataInputStream dis) throws IOException {
        while (dis.available() != 0) {
            SectionType type = SectionType.valueOf(read(dis));
            Map<SectionType, Section> sections = resume.getSections();
            switch (type) {
                case OBJECTIVE, PERSONAL -> sections.put(type, new TextSection(read(dis)));
                case ACHIEVEMENT, QUALIFICATIONS -> sections.put(type, new ListSection(getItems(dis)));
                case EXPERIENCE, EDUCATION -> sections.put(type, new CompanySection(getCompanies(dis)));
            }
        }
    }

    private List<Company> getCompanies(DataInputStream dis) {
        return Arrays.stream(new Company[readSize(dis)])
                .map(x -> {
                    String name = read(dis);
                    String website = read(dis);
                    return new Company(name, website, getPeriods(dis));
                })
                .collect(Collectors.toList());
    }

    private List<Company.Period> getPeriods(DataInputStream dis) {
        return Arrays.stream(new Company.Period[readSize(dis)])
                .map(x -> {
                    LocalDate beginDate = LocalDate.parse(read(dis));
                    LocalDate endDate = LocalDate.parse(read(dis));
                    String title = read(dis);
                    List<String> description = getItems(dis);
                    return new Company.Period(beginDate, endDate, title, description);
                })
                .collect(Collectors.toList());
    }

    private void writeItems(List<String> items, DataOutputStream dos) {
        writeSize(items.size(), dos);
        items.forEach(x -> write(x, dos));
    }

    private List<String> getItems(DataInputStream dis) {
        return Arrays.stream(new String[readSize(dis)])
                .map(x -> read(dis))
                .collect(Collectors.toList());
    }

    private void write(String s, DataOutputStream dos) {
        try {
            dos.writeUTF(s);
        } catch (IOException e) {
            throw new StorageException("File write error", e);
        }
    }

    private void writeSize(int size, DataOutputStream dos) {
        try {
            dos.writeInt(size);
        } catch (IOException e) {
            throw new StorageException("File write error", e);
        }
    }

    private String read(DataInputStream dis) {
        try {
            return dis.readUTF();
        } catch (IOException e) {
            throw new StorageException("File read error", e);
        }
    }

    private int readSize(DataInputStream dis) {
        try {
            return dis.readInt();
        } catch (IOException e) {
            throw new StorageException("File read error", e);
        }
    }
}

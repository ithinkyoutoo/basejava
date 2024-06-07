package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.BiCustomConsumer;
import ru.javawebinar.basejava.util.CustomConsumer;

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
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
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

    private void writeContacts(Map<ContactType, String> contacts, DataOutputStream dos) throws IOException {
        write(contacts, dos, (type, text) -> {
            dos.writeUTF(type.name());
            dos.writeUTF(text);
        });
    }

    private void writeSections(Map<SectionType, Section> sections, DataOutputStream dos) throws IOException {
        write(sections, dos, (type, section) -> {
            dos.writeUTF(type.name());
            switch (type) {
                case OBJECTIVE, PERSONAL -> write((TextSection) section, s -> dos.writeUTF(s.getText()));
                case ACHIEVEMENT, QUALIFICATIONS ->
                        write((ListSection) section, s -> write(s.getItems(), dos, dos::writeUTF));
                case EXPERIENCE, EDUCATION ->
                        write((CompanySection) section, s -> writeCompanies(s.getCompanies(), dos));
            }
        });
    }

    private void writeCompanies(List<Company> companies, DataOutputStream dos) throws IOException {
        write(companies, dos, c -> {
            dos.writeUTF(c.getName());
            String website = c.getWebsite();
            if (isNonNull(website, dos)) {
                dos.writeUTF(website);
            }
            writePeriods(c.getPeriods(), dos);
        });
    }

    private void writePeriods(List<Company.Period> periods, DataOutputStream dos) throws IOException {
        write(periods, dos, p -> {
            dos.writeUTF(p.getBeginDate().toString());
            dos.writeUTF(p.getEndDate().toString());
            dos.writeUTF(p.getTitle());
            List<String> description = p.getDescription();
            if (isNonNull(description, dos)) {
                write(description, dos, (dos::writeUTF));
            }
        });
    }

    private <K, V> void write(Map<K, V> map, DataOutputStream dos, BiCustomConsumer<K, V> action) throws IOException {
        dos.writeInt(map.size());
        for (Map.Entry<K, V> entry : map.entrySet())
            action.accept(entry.getKey(), entry.getValue());
    }

    private <T> void write(T section, CustomConsumer<T> action) throws IOException {
        action.accept(section);
    }

    private <T> void write(List<T> list, DataOutputStream dos, CustomConsumer<T> action) throws IOException {
        dos.writeInt(list.size());
        for (T t : list) {
            action.accept(t);
        }
    }

    private <T> boolean isNonNull(T object, DataOutputStream dos) throws IOException {
        if (object != null) {
            dos.writeBoolean(true);
            return true;
        }
        dos.writeBoolean(false);
        return false;
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
        readSize(dis);
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
                    if (readBoolean(dis)) {
                        String website = read(dis);
                        return new Company(name, website, getPeriods(dis));
                    }
                    return new Company(name, getPeriods(dis));
                })
                .collect(Collectors.toList());
    }

    private List<Company.Period> getPeriods(DataInputStream dis) {
        return Arrays.stream(new Company.Period[readSize(dis)])
                .map(x -> {
                    LocalDate beginDate = LocalDate.parse(read(dis));
                    LocalDate endDate = LocalDate.parse(read(dis));
                    String title = read(dis);
                    if (readBoolean(dis)) {
                        List<String> description = getItems(dis);
                        return new Company.Period(beginDate, endDate, title, description);
                    }
                    return new Company.Period(beginDate, endDate, title);
                })
                .collect(Collectors.toList());
    }

    private List<String> getItems(DataInputStream dis) {
        return Arrays.stream(new String[readSize(dis)])
                .map(x -> read(dis))
                .collect(Collectors.toList());
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

    private boolean readBoolean(DataInputStream dis) {
        try {
            return dis.readBoolean();
        } catch (IOException e) {
            throw new StorageException("File read error", e);
        }
    }
}

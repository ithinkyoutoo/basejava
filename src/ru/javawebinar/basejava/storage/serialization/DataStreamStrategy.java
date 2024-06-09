package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.BiCustomConsumer;
import ru.javawebinar.basejava.util.CustomConsumer;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readContacts(resume.getContacts(), dis);
            readSections(resume.getSections(), dis);
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

    private void readContacts(Map<ContactType, String> contacts, DataInputStream dis) throws IOException {
        read(contacts, dis, c -> c.put(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
    }

    private void readSections(Map<SectionType, Section> sections, DataInputStream dis) throws IOException {
        read(sections, dis, s -> {
            SectionType type = SectionType.valueOf(dis.readUTF());
            switch (type) {
                case OBJECTIVE, PERSONAL -> s.put(type, new TextSection(dis.readUTF()));
                case ACHIEVEMENT, QUALIFICATIONS -> s.put(type, new ListSection(getItems(dis)));
                case EXPERIENCE, EDUCATION -> s.put(type, new CompanySection(getCompanies(dis)));
            }
        });
    }

    private List<Company> getCompanies(DataInputStream dis) throws IOException {
        return readList(dis, c -> {
            String name = dis.readUTF();
            if (dis.readBoolean()) {
                String website = dis.readUTF();
                c.add(new Company(name, website, getPeriods(dis)));
            } else {
                c.add(new Company(name, getPeriods(dis)));
            }
        });
    }

    private List<Company.Period> getPeriods(DataInputStream dis) throws IOException {
        return readList(dis, p -> {
            LocalDate beginDate = LocalDate.parse(dis.readUTF());
            LocalDate endDate = LocalDate.parse(dis.readUTF());
            String title = dis.readUTF();
            if (dis.readBoolean()) {
                List<String> description = getItems(dis);
                p.add(new Company.Period(beginDate, endDate, title, description));
            } else {
                p.add(new Company.Period(beginDate, endDate, title));
            }
        });
    }

    private List<String> getItems(DataInputStream dis) throws IOException {
        return readList(dis, s -> s.add(dis.readUTF()));
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

    private <T> void read(T map, DataInputStream dis, CustomConsumer<T> action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.accept(map);
        }
    }

    private <T> List<T> readList(DataInputStream dis, CustomConsumer<List<T>> action) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            action.accept(list);
        }
        return list;
    }
}

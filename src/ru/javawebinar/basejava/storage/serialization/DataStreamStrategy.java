package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.CustomConsumer;
import ru.javawebinar.basejava.util.CustomRunnable;
import ru.javawebinar.basejava.util.CustomSupplier;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataStreamStrategy implements Serialization {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            writeContacts(resume, dos);
            writeSections(resume, dos);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            addContacts(resume, dis);
            addSections(resume, dis);
            return resume;
        }
    }

    private void writeContacts(Resume resume, DataOutputStream dos) throws IOException {
        write(resume.getContacts().entrySet(), dos, contact -> {
            dos.writeUTF(contact.getKey().name());
            dos.writeUTF(contact.getValue());
        });
    }

    private void writeSections(Resume resume, DataOutputStream dos) throws IOException {
        write(resume.getSections().entrySet(), dos, entry -> {
            SectionType type = entry.getKey();
            Section section = entry.getValue();
            dos.writeUTF(type.name());
            switch (type) {
                case OBJECTIVE, PERSONAL -> dos.writeUTF(((TextSection) section).getText());
                case ACHIEVEMENT, QUALIFICATIONS -> write(((ListSection) section).getItems(), dos, dos::writeUTF);
                case EXPERIENCE, EDUCATION -> writeCompanies(((CompanySection) section).getCompanies(), dos);
            }
        });
    }

    private void writeCompanies(List<Company> companies, DataOutputStream dos) throws IOException {
        write(companies, dos, company -> {
            dos.writeUTF(company.getName());
            String website = company.getWebsite();
            if (isNonNull(website, dos)) {
                dos.writeUTF(website);
            }
            write(company.getPeriods(), dos, period -> {
                writeLocalDate(period.getBeginDate(), dos);
                writeLocalDate(period.getEndDate(), dos);
                dos.writeUTF(period.getTitle());
                List<String> description = period.getDescription();
                if (isNonNull(description, dos)) {
                    write(description, dos, dos::writeUTF);
                }
            });
        });
    }

    private void addContacts(Resume resume, DataInputStream dis) throws IOException {
        read(dis, () -> resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
    }

    private void addSections(Resume resume, DataInputStream dis) throws IOException {
        read(dis, () -> {
            SectionType type = SectionType.valueOf(dis.readUTF());
            resume.setSection(type, switch (type) {
                        case OBJECTIVE, PERSONAL -> new TextSection(dis.readUTF());
                        case ACHIEVEMENT, QUALIFICATIONS -> new ListSection(readItems(dis));
                        case EXPERIENCE, EDUCATION -> new CompanySection(readCompanies(dis));
                    }
            );
        });
    }

    private List<Company> readCompanies(DataInputStream dis) throws IOException {
        return readList(dis, () -> {
            String name = dis.readUTF();
            if (dis.readBoolean()) {
                String website = dis.readUTF();
                return new Company(name, website, readPeriods(dis));
            }
            return new Company(name, readPeriods(dis));
        });
    }

    private List<Company.Period> readPeriods(DataInputStream dis) throws IOException {
        return readList(dis, () -> {
            LocalDate beginDate = readLocalDate(dis);
            LocalDate endDate = readLocalDate(dis);
            String title = dis.readUTF();
            if (dis.readBoolean()) {
                List<String> description = readItems(dis);
                return new Company.Period(beginDate, endDate, title, description);
            }
            return new Company.Period(beginDate, endDate, title);
        });
    }

    private <T> void write(Collection<T> collection, DataOutputStream dos, CustomConsumer<T> action) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            action.accept(item);
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

    private void writeLocalDate(LocalDate date, DataOutputStream dos) throws IOException {
        dos.writeInt(date.getYear());
        dos.writeInt(date.getMonth().getValue());
    }

    private void read(DataInputStream dis, CustomRunnable action) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            action.run();
        }
    }

    private List<String> readItems(DataInputStream dis) throws IOException {
        return readList(dis, dis::readUTF);
    }

    private <T> List<T> readList(DataInputStream dis, CustomSupplier<T> action) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(action.get());
        }
        return list;
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }
}

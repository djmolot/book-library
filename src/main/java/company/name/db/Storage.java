package company.name.db;

import company.name.models.Book;
import company.name.models.Reader;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final List<Book> books = new ArrayList<>();
    private static final List<Reader> readers = new ArrayList<>();

    public static List<Book> getBooks() {
        return books;
    }

    public static List<Reader> getReaders() {
        return readers;
    }
}

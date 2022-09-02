package company.name.db;

import company.name.models.Book;
import company.name.models.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {
    private static final List<Book> books = new ArrayList<>();
    private static final List<Reader> readers = new ArrayList<>();
    //@OneToMany
    private static final Map<Long, List<Long>> readers_books = new HashMap<>();

    public static List<Book> getBooks() {
        return books;
    }

    public static List<Reader> getReaders() {
        return readers;
    }

    public static Map<Long, List<Long>> getReaders_Books() {
        return readers_books;
    }
}

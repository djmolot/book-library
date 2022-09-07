package company.name.db;

import company.name.models.Book;
import company.name.models.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {
    private static final List<Book> books = new ArrayList<>();
    private static Long booksSize = 0L;
    private static final List<Reader> readers = new ArrayList<>();
    private static Long readersSize = 0L;
    //@OneToMany
    private static final Map<Long, List<Long>> readers_books = new HashMap<>();

    public static List<Book> getBooks() {
        return books;
    }

    public static Long getBooksSize() {
        return booksSize;
    }

    public static void setBooksSize(Long booksSize) {
        Storage.booksSize = booksSize;
    }

    public static List<Reader> getReaders() {
        return readers;
    }

    public static Long getReadersSize() {
        return readersSize;
    }

    public static void setReadersSize(Long readersSize) {
        Storage.readersSize = readersSize;
    }

    public static Map<Long, List<Long>> getReaders_Books() {
        return readers_books;
    }
}

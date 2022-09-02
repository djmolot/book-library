package company.name.service;

import company.name.models.Book;
import company.name.models.Reader;

import java.util.List;

public interface LibraryService {
    void borrowBookForReader(Reader reader, Book book);

    void returnBookFromReader(Reader reader, Book book);

    Reader getCurrentReaderOfBook(Book book);

    List<Book> getAllBooksByReader(Reader reader);
}

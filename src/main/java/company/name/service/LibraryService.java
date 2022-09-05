package company.name.service;

import company.name.models.Book;
import company.name.models.Reader;

import java.util.List;

public interface LibraryService {
    void borrowBookForReader(Reader reader, Book book);

    void returnBookFromReader(Reader reader, Book book);

    Reader getCurrentReaderOfBook(Book book);

    List<Book> getAllBooksByReader(Reader reader);

    void doMenu1Handler();

    void doMenu2Handler();

    void doMenu3Handler(String readerName);

    void doMenu4Handler(String bookInput);

    void doMenu5Handler(String input);

    void doMenu6Handler(String input);

    void doMenu7Handler(String input);

    void doMenu8Handler(String input);

    void prepareLibraryData();
}

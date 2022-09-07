package company.name.service;

import company.name.models.Book;
import company.name.models.Reader;

import java.util.List;

public interface LibraryService {
    void borrowBookForReader(Long readerID, Long bookId);

    void returnBookFromReader(Long readerID, Long bookId);

    Reader getCurrentReaderOfBook(Book book);

    List<Book> getAllBooksByReader(Reader reader);

    void showAllBooks();

    void showAllReaders();

    void registerNewReader();

    void addNewBook();

    void borrowBookToReader();

    void returnBookToLibrary();

    void showAllBooksByReader();

    void showReaderOfBook();

    void prepareLibraryData();
}

package company.name.service;

import company.name.models.Book;
import company.name.models.Reader;

import java.util.List;

public interface LibraryService {
    void borrowBookForReader(Long bookId, Long readerId);

    void returnBookFromReader(Long bookId, Long readerId);

    Reader getCurrentReaderOfBook(Long bookId);

    List<Book> getAllBooksByReader(Long readerId);

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

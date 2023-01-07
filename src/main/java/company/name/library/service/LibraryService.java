package company.name.library.service;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import java.util.List;
import java.util.Optional;

public interface LibraryService {
    List<Book> getAllBooks();

    List<Reader> getAllReaders();

    Reader registerNewReader(String input);

    Book addNewBook(Book book);

    Book borrowBookToReader(Long bookId, Long readerId);

    Book returnBookToLibrary(Long bookId);

    List<Book> getAllBooksOfReader(String input);

    Optional<Reader> getReaderOfBookWithId(Long bookId);
}

package company.name.service;

import company.name.entities.Book;
import company.name.entities.Reader;
import java.util.List;
import java.util.Optional;

public interface LibraryService {
    List<Book> getAllBooks();

    List<Reader> getAllReaders();

    Reader registerNewReader(String input);

    Book addNewBook(String input);

    void borrowBookToReader(String input);

    void returnBookToLibrary(String input);

    List<Book> getAllBooksOfReader(String input);

    Optional<Reader> getReaderOfBookWithId(String input);
}

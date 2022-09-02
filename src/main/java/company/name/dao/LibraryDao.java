package company.name.dao;

import company.name.models.Book;
import company.name.models.Reader;
import java.util.List;

public interface LibraryDao {
    void borrowBookForReader(Reader reader, Book book);

    void returnBookFromReader(Reader reader, Book book);

    List<Book> getAllBooksByReader(Reader reader);

    Reader getCurrentReaderOfBook(Book book);
}

package company.name.dao;

import company.name.models.Book;
import company.name.models.Reader;
import java.util.List;

public interface ReaderDao {
    void add(Reader reader);

    Reader get(Long id);

    List<Reader> getAll();

    void update(Reader reader);

    List<Long> getBorrowedBooksIds(Reader reader);

    Reader getCurrentReaderOfBook(Book book);

}

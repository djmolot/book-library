package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import company.name.models.Reader;
import java.util.List;

public interface ReaderDao {
    void add(Reader reader);

    Reader get(Long id);

    List<Reader> getAll();

    void update(Reader reader);

    Reader getCurrentReaderOfBook(Book book);

}

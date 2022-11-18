package company.name.dao;

import company.name.entities.Reader;
import java.util.List;
import java.util.Optional;

public interface ReaderDao {
    Reader add(Reader reader);

    Optional<Reader> getById(Long id);

    List<Reader> getAll();

    void update(Reader reader);

    Optional<Reader> getReaderByBookId(Long bookId);
}

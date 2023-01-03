package company.name.library.repositories;

import company.name.library.entities.Reader;
import java.util.List;
import java.util.Optional;

public interface ReaderRepository {
    Reader add(Reader reader);

    Optional<Reader> getById(Long id);

    List<Reader> getAll();

    void update(Reader reader);

    Optional<Reader> getReaderByBookId(Long bookId);
}

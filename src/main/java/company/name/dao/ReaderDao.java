package company.name.dao;

import company.name.exceptions.DaoLayerException;
import company.name.entities.Reader;
import java.util.List;
import java.util.Optional;

public interface ReaderDao {
    Reader add(Reader reader) throws DaoLayerException;

    Optional<Reader> getById(Long id);

    List<Reader> getAll() throws DaoLayerException;

    void update(Reader reader);

    Optional<Reader> getReaderByBookId(Long bookId);

}

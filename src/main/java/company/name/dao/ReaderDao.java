package company.name.dao;

import company.name.models.Reader;
import java.util.List;
import java.util.Optional;

public interface ReaderDao {
    void add(Reader reader);

    Optional<Reader> getById(Long id);

    List<Reader> getAll();

    void update(Reader reader);

    boolean containsReaderWithId(Long id);

}

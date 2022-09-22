package company.name.dao;

import java.util.List;
import java.util.Optional;

public interface LibraryDao {
    void borrowBookForReader(Long bookId, Long readerId);

    void returnBookFromReader(Long bookId, Long readerId);

    Optional<Long> getReaderIdByBookId(Long bookId);

    List<Long> getBooksIdsByReaderId(Long readerId);
}

package company.name.dao;

import java.util.List;

public interface LibraryDao {
    void borrowBookForReader(Long bookId, Long readerId);

    void returnBookFromReader(Long bookId, Long readerId);

    Long getReaderIdByBookId(Long bookId);

    List<Long> getBooksIdsByReaderId(Long readerId);
}

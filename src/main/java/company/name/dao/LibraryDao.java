package company.name.dao;

public interface LibraryDao {
    void borrowBookForReader(Long readerId, Long bookId);

    void returnBookFromReader(Long readerId, Long bookId);
}

package company.name.dao;

public interface LibraryDao {
    void borrowBookForReader(Long bookId, Long readerId);

    void returnBookFromReader(Long bookId, Long readerId);
}

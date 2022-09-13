package company.name.dao;

import company.name.db.Storage;
import java.util.List;

public class LibraryDaoImpl implements LibraryDao {

    @Override
    public void borrowBookForReader(Long bookId, Long readerId) {
        List<Long> readerBooks = Storage.getReaders_Books().get(readerId);
        readerBooks.add(bookId);
    }

    @Override
    public void returnBookFromReader(Long bookId, Long readerId) {
        List<Long> readerBooks = Storage.getReaders_Books().get(readerId);
        readerBooks.remove(bookId);
    }

    @Override
    public Long getReaderIdByBookId(Long bookId) {
        return Storage.getReaders_Books().entrySet().stream()
                .filter(entry -> entry.getValue().contains(bookId))
                .findFirst()
                .get().getKey();
    }

    @Override
    public List<Long> getBooksIdsByReaderId(Long readerId) {
        return Storage.getReaders_Books().get(readerId);
    }
}

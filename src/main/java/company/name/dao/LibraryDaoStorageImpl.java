package company.name.dao;

import company.name.db.Storage;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LibraryDaoStorageImpl implements LibraryDao {

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
    public Optional<Long> getReaderIdByBookId(Long bookId) {
        return Storage.getReaders_Books().entrySet().stream()
                .filter(entry -> entry.getValue().contains(bookId))
                .findFirst()
                .map(Map.Entry::getKey);
    }

    @Override
    public List<Long> getBooksIdsByReaderId(Long readerId) {
        return Storage.getReaders_Books().get(readerId);
    }
}

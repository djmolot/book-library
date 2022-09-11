package company.name.dao;

import company.name.db.Storage;
import java.util.List;

public class LibraryDaoImpl implements LibraryDao {

    @Override
    public void borrowBookForReader(Long bookId, Long readerID) {
        List<Long> readerBooks = Storage.getReaders_Books().get(readerID);
        readerBooks.add(bookId);
    }

    @Override
    public void returnBookFromReader(Long bookId, Long readerID) {
        List<Long> readerBooks = Storage.getReaders_Books().get(readerID);
        readerBooks.remove(bookId);
    }

}

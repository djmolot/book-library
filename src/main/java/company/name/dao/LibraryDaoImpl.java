package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import company.name.models.Reader;
import java.util.List;

public class LibraryDaoImpl implements LibraryDao {

    @Override
    public void borrowBookForReader(Reader reader, Book book) {
        List<Long> readerBooks = Storage.getReaders_Books().get(reader.getId());
        readerBooks.add(book.getId());
    }

    @Override
    public void returnBookFromReader(Reader reader, Book book) {
        List<Long> readerBooks = Storage.getReaders_Books().get(reader.getId());
        readerBooks.remove(book.getId());
    }

}

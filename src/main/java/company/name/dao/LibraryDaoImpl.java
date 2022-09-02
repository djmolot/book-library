package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import company.name.models.Reader;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryDaoImpl implements LibraryDao {
    private BookDao bookDao;
    private ReaderDao readerDao;

    public LibraryDaoImpl(BookDao bookDao, ReaderDao readerDao) {
        this.bookDao = bookDao;
        this.readerDao = readerDao;
    }

    @Override
    public void borrowBookForReader(Reader reader, Book book) {
        List<Long> readerBooks = Storage.getReaders_Books().get(reader.getId());
        readerBooks.add(book.getId());
    }

    @Override
    public void returnBookFromReader(Reader reader, Book book) {
        List<Long> readerBooks = Storage.getReaders_Books().get(reader.getId());
        readerBooks.remove(book.getId());
        readerDao.get(reader.getId()).getBorowedBooks().remove(book.getId());
    }

    @Override
    public List<Book> getAllBooksByReader(Reader reader) {
        List<Long> readerBooksIds = Storage.getReaders_Books().get(reader.getId());
        return readerBooksIds.stream()
                .map(id -> bookDao.get(id))
                .collect(Collectors.toList());
    }

    @Override
    public Reader getCurrentReaderOfBook(Book book) {
        Long readerId = Storage.getReaders_Books().entrySet().stream()
                .filter(e -> e.getValue().contains(book.getId()))
                .findFirst()
                .get()
                .getKey();
        return readerDao.get(readerId);
    }
}

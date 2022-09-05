package company.name.service;

import company.name.dao.BookDao;
import company.name.dao.LibraryDao;
import company.name.dao.ReaderDao;
import company.name.models.Book;
import company.name.models.Reader;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryServiceImpl implements LibraryService {
    private final LibraryDao libraryDao;
    private final BookDao bookDao;
    private final ReaderDao readerDao;

    public LibraryServiceImpl(LibraryDao libraryDao, BookDao bookDao, ReaderDao readerDao) {
        this.libraryDao = libraryDao;
        this.bookDao = bookDao;
        this.readerDao = readerDao;
    }

    @Override
    public void borrowBookForReader(Reader reader, Book book) {
        libraryDao.borrowBookForReader(reader, book);
    }

    @Override
    public void returnBookFromReader(Reader reader, Book book) {
        libraryDao.returnBookFromReader(reader, book);
    }

    @Override
    public Reader getCurrentReaderOfBook(Book book) {
        return readerDao.getCurrentReaderOfBook(book);
    }

    @Override
    public List<Book> getAllBooksByReader(Reader reader) {
        return readerDao.getBorrowedBooksIds(reader).stream()
                .map(id -> bookDao.get(id))
                .collect(Collectors.toList());
    }
}

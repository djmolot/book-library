package company.name.service;

import company.name.dao.LibraryDao;
import company.name.models.Book;
import company.name.models.Reader;
import java.util.List;

public class LibraryServiceImpl implements LibraryService {
    private LibraryDao libraryDao;

    public LibraryServiceImpl(LibraryDao libraryDao) {
        this.libraryDao = libraryDao;
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
        return libraryDao.getCurrentReaderOfBook(book);
    }

    @Override
    public List<Book> getAllBooksByReader(Reader reader) {
        return libraryDao.getAllBooksByReader(reader);
    }
}

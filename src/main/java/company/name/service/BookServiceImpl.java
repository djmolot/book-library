package company.name.service;

import company.name.dao.BookDao;
import company.name.models.Book;
import company.name.models.Reader;

import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public void createNewBook(Book book) {
        bookDao.add(book);
    }

    @Override
    public Book get(Long id) {
        return bookDao.get(id);
    }

    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }

}

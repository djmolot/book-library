package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import company.name.models.Reader;

import java.util.List;

public interface BookDao {
    void add(Book book);

    Book get(Long id);

    List<Book> getAll();

    List<Book> getBorrowedBooksByReaderId(Long readerId);

}

package company.name.dao;

import company.name.models.Book;

import java.util.List;

public interface BookDao {
    void add(Book book);

    boolean containsBookWithId(Long id);

    Book get(Long id);

    List<Book> getAll();

    List<Book> getBorrowedBooksByReaderId(Long readerId);

}

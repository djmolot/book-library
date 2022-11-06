package company.name.dao;

import company.name.entities.Book;
import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book add(Book book);

    Optional<Book> getById(Long id);

    List<Book> getAll();

    Book update(Book book);

    List<Book> getBooksByReaderId(Long readerId);

}

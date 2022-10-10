package company.name.dao;

import company.name.models.Book;
import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book add(Book book);

    Optional<Book> getById(Long id);

    List<Book> getAll();

    boolean update(Book book);

    List<Book> getBooksByReaderId(Long readerId);

}

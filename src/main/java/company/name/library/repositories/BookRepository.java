package company.name.library.repositories;

import company.name.library.entities.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book add(Book book);

    Optional<Book> getById(Long id);

    boolean deleteById(Long id);

    List<Book> getAll();

    Book update(Book book);

    List<Book> getBooksByReaderId(Long readerId);

}

package company.name.dao;

import company.name.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    void add(Book book);

    boolean containsBookWithId(Long id);

    Optional<Book> getById(Long id);

    List<Book> getAll();

}

package company.name.dao;

import company.name.models.Book;
import java.util.List;

public interface BookDao {
    void add(Book book);

    Book get(Long id);

    List<Book> getAll();

    void update(Book book);
}

package company.name.service;

import company.name.models.Book;
import java.util.List;

public interface BookService {
    void createNewBook(Book book);

    List<Book> getAll();
}

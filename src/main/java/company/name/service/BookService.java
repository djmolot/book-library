package company.name.service;

import company.name.models.Book;
import java.util.List;

public interface BookService {
    void createNewBook(String name, String author);

    List<Book> getAll();
}

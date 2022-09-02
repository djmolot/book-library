package company.name.service;

import company.name.models.Book;
import company.name.models.Reader;

import java.util.List;

public interface BookService {
    void createNewBook(Book book);

    Book get(Long id);

    List<Book> getAll();
}

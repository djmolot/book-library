package company.name.dao;

import company.name.models.Book;
import company.name.models.Reader;

public interface LibraryDao {
    void borrowBookForReader(Reader reader, Book book);

    void returnBookFromReader(Reader reader, Book book);
}

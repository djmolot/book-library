package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    @Override
    public void add(Book book) {
        int size = Storage.books.size();
        book.setId(size + 1L);
        Storage.books.add(book);
    }

    @Override
    public Book get(Long id) {
        return Storage.books.stream().
                filter(book -> book.getId().equals(id)).
                findFirst().get();
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>(Storage.books);
    }

    @Override
    public void update(Book book) {
        Book bookFromDB = get(book.getId());
        Storage.books.remove(bookFromDB);
        add(book);
    }
}

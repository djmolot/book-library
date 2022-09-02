package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import java.util.ArrayList;
import java.util.List;

public class BookDaoStorageImpl implements BookDao {

    @Override
    public void add(Book book) {
        int size = Storage.getBooks().size();
        book.setId(size + 1L);
        Storage.getBooks().add(book);
    }

    @Override
    public Book get(Long id) {
        return Storage.getBooks().stream().
                filter(book -> book.getId().equals(id)).
                findFirst().get();
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>(Storage.getBooks());
    }

}

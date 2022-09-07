package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import java.util.ArrayList;
import java.util.List;

public class BookDaoStorageImpl implements BookDao {
    private final Long ID_OFFSET = 1000L;

    @Override
    public void add(Book book) {
        Storage.setBooksSize(Storage.getBooksSize() + 1L);;
        book.setId(Storage.getBooksSize() + ID_OFFSET);
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

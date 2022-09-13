package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BookDaoStorageImpl implements BookDao {
    private final Long ID_OFFSET = 1000L;

    @Override
    public void add(Book book) {
        Storage.setBooksSize(Storage.getBooksSize() + 1L);;
        book.setId(Storage.getBooksSize() + ID_OFFSET);
        Storage.getBooks().add(book);
    }

    @Override
    public boolean containsBookWithId(Long id) {
        return Storage.getBooks().stream().anyMatch(book -> book.getId().equals(id));
    }

    @Override
    public Optional<Book> getById(Long id) {
        return Storage.getBooks().stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>(Storage.getBooks());
    }

}

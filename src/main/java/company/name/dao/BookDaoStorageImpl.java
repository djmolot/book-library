package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class BookDaoStorageImpl implements BookDao {
    private static final AtomicLong idGenerator = new AtomicLong(1000);

    @Override
    public void add(Book book) {
        book.setId(idGenerator.incrementAndGet());
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

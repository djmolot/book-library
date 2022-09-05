package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import company.name.models.Reader;
import java.util.ArrayList;
import java.util.List;

public class ReaderDaoStorageImpl implements ReaderDao {
    @Override
    public void add(Reader reader) {
        int size = Storage.getReaders().size();
        reader.setId(size + 1L);
        Storage.getReaders().add(reader);
        Storage.getReaders_Books().put(reader.getId(), new ArrayList<>());

    }

    @Override
    public Reader get(Long id) {
        return Storage.getReaders().stream().
                filter(reader -> reader.getId().equals(id)).
                findFirst().get();
    }

    @Override
    public List<Reader> getAll() {
        return new ArrayList<>(Storage.getReaders());
    }

    @Override
    public void update(Reader reader) {
        Reader readerFromDB = get(reader.getId());
        Storage.getReaders().remove(readerFromDB);
        add(reader);
    }

    @Override
    public List<Long> getBorrowedBooksIds(Reader reader) {
        return Storage.getReaders_Books().get(reader.getId());
    }

    @Override
    public Reader getCurrentReaderOfBook(Book book) {
        Long readerId = Storage.getReaders_Books().entrySet().stream()
                .filter(e -> e.getValue().contains(book.getId()))
                .findFirst()
                .get()
                .getKey();
        return get(readerId);
    }
}

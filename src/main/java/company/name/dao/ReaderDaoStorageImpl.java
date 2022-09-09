package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import company.name.models.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ReaderDaoStorageImpl implements ReaderDao {
    private final Long ID_OFFSET = 1000L;

    @Override
    public void add(Reader reader) {
        Storage.setReadersSize(Storage.getReadersSize() + 1L);;
        reader.setId(Storage.getReadersSize() + ID_OFFSET);
        Storage.getReaders().add(reader);
        Storage.getReaders_Books().put(reader.getId(), new ArrayList<>());

    }

    @Override
    public boolean containsReaderWithId(Long id) {
        return Storage.getReaders().stream().anyMatch(reader -> reader.getId().equals(id));
    }

    @Override
    public Reader get(Long id) {
        if(!this.containsReaderWithId(id)) {
            throw new NoSuchElementException("Reader with id " + id + " does not exists in the storage");
        } else {
            return Storage.getReaders().stream()
                    .filter(reader -> reader.getId().equals(id))
                    .findFirst()
                    .get();
        }
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
    public Reader getCurrentReaderOfBook(Long bookId) {
        Long readerId = Storage.getReaders_Books().entrySet().stream()
                .filter(e -> e.getValue().contains(bookId))
                .findFirst()
                .get()
                .getKey();
        return get(readerId);
    }
}

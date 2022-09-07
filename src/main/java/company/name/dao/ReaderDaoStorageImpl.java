package company.name.dao;

import company.name.db.Storage;
import company.name.models.Book;
import company.name.models.Reader;
import java.util.ArrayList;
import java.util.List;

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
    public Reader getCurrentReaderOfBook(Book book) {
        Long readerId = Storage.getReaders_Books().entrySet().stream()
                .filter(e -> e.getValue().contains(book.getId()))
                .findFirst()
                .get()
                .getKey();
        return get(readerId);
    }
}

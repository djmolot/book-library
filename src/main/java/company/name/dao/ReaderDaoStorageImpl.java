package company.name.dao;

import company.name.db.Storage;
import company.name.models.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<Reader> getById(Long id) {
        return Storage.getReaders().stream()
                .filter(reader -> reader.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Reader> getAll() {
        return new ArrayList<>(Storage.getReaders());
    }

    @Override
    public void update(Reader reader) {
        Reader readerFromDB = getById(reader.getId()).get();
        Storage.getReaders().remove(readerFromDB);
        add(reader);
    }

}

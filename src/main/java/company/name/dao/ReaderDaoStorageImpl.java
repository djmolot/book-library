package company.name.dao;

import company.name.db.Storage;
import company.name.models.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ReaderDaoStorageImpl implements ReaderDao {
    private static final AtomicLong idGenerator = new AtomicLong(1000);

    @Override
    public void add(Reader reader) {
        reader.setId(idGenerator.incrementAndGet());
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
    public void update(Reader updatedReader) {
        getById(updatedReader.getId())
                .ifPresentOrElse(
                        existingReader -> {
                            Storage.getReaders().remove(existingReader);
                            add(updatedReader);
                        },
                        () -> System.err.println("Reader with ID " + updatedReader.getId()
                                + " does not exist. Failed to update.")
                );
    }

}

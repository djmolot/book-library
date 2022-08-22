package company.name.dao;

import company.name.db.Storage;
import company.name.models.Reader;
import java.util.ArrayList;
import java.util.List;

public class ReaderDaoStorageImpl implements ReaderDao {
    @Override
    public void add(Reader reader) {
        int size = Storage.getReaders().size();
        reader.setId(size + 1L);
        Storage.getReaders().add(reader);
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
}

package company.name.dao;

import company.name.db.Storage;
import company.name.models.Reader;
import java.util.ArrayList;
import java.util.List;

public class ReaderDaoImpl implements ReaderDao {
    @Override
    public void add(Reader reader) {
        int size = Storage.readers.size();
        reader.setId(size + 1L);
        Storage.readers.add(reader);
    }

    @Override
    public Reader get(Long id) {
        return Storage.readers.stream().
                filter(reader -> reader.getId().equals(id)).
                findFirst().get();
    }

    @Override
    public List<Reader> getAll() {
        return new ArrayList<>(Storage.readers);
    }

    @Override
    public void update(Reader reader) {
        Reader readerFromDB = get(reader.getId());
        Storage.readers.remove(readerFromDB);
        add(reader);
    }
}

package company.name.service;

import company.name.dao.ReaderDao;
import company.name.models.Reader;
import java.util.List;

public class ReaderServiceImpl implements ReaderService {
    private final ReaderDao readerDao;

    public ReaderServiceImpl(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    @Override
    public void createNewReader(Reader reader) {
        readerDao.add(reader);
    }

    @Override
    public List<Reader> getAll() {
        return readerDao.getAll();
    }
}

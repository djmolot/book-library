package company.name.service;

import company.name.models.Reader;
import java.util.List;

public interface ReaderService {
    void createNewReader(Reader reader);

    Reader get(Long id);

    List<Reader> getAll();

}

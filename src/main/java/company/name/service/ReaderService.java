package company.name.service;

import company.name.models.Reader;
import java.util.List;

public interface ReaderService {
    void createNewReader(String name);

    List<Reader> getAll();

}

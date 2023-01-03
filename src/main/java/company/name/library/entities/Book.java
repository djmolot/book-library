package company.name.library.entities;

import lombok.Data;
import java.util.Optional;

@Data
public class Book {
    private Long id;
    private String author;
    private String title;
    //@OneToOne
    private Optional<Reader> reader;
}

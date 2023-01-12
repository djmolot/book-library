package company.name.library.entities;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Optional;

@Data
public class Book {

    private Long id;

    @Size(min = 3, max = 50, message
            = "Author must be between 3 and 50 characters")
    private String author;

    @Size(min = 10, max = 255, message
            = "Title must be between 10 and 255 characters")
    private String title;

    //@OneToOne
    private Optional<Reader> reader;
}

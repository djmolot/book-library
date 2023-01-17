package company.name.library.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class Reader {
    private Long id;

    @NotNull(message = "Please provide a name of reader")
    @Size(min = 3, max = 50, message
            = "Reader name must be between 3 and 50 characters")
    private String name;

    //@OneToMany
    private List<Book> books;
}

package company.name.library.entities;

import lombok.Data;
import java.util.List;

@Data
public class Reader {
    private Long id;
    private String name;
    //@OneToMany
    private List<Book> books;
}

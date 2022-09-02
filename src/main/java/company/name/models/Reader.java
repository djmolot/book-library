package company.name.models;

import java.util.ArrayList;
import java.util.List;

public class Reader {
    private Long id;
    private String name;
    //@OneToMany
    private final List<Long> borowedBooks;

    public Reader() {
        this.borowedBooks = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getBorowedBooks() {
        return borowedBooks;
    }

    @Override
    public String toString() {
        return (id + ". " + name);
    }
}

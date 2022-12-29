package company.name.library.entities;

import java.util.List;

public class Reader {
    private Long id;
    private String name;
    //@OneToMany
    private List<Book> books;

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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass().equals(Reader.class)) {
            Reader casted = (Reader) obj;
            if (casted.id == null) {
                return this.name.equals(casted.name);
            } else {
                return this.id.equals(casted.id) && this.name.equals(casted.name);
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return (id + ". " + name);
    }
}

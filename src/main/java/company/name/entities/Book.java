package company.name.entities;

import java.util.Objects;
import java.util.Optional;

public class Book {
    private Long id;
    private String title;
    private String author;
    //@OneToOne
    private Optional<Reader> reader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Optional<Reader> getReader() {
        return reader;
    }

    public void setReader(Optional<Reader> reader) {
        this.reader = reader;
    }

    @Override
    public String toString() {
        return (id + ". " + author + ". \"" + title + ".\"");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book casted = (Book) o;
        if (casted.id == null) {
            return title.equals(casted.title) && author.equals(casted.author);
        } else {
            return id.equals(casted.id) && title.equals(casted.title) && author.equals(casted.author);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, reader);
    }
}

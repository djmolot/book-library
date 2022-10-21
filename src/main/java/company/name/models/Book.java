package company.name.models;

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

}

package company.name.models;

public class Book {
    private Long id;
    private String title;
    private String author;
    //@OneToOne
    private Reader reader;

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

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }


    @Override
    public String toString() {
        return (id + ". " + author + ". \"" + title + ".\"");
    }

}

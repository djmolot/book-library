
package company.name.library;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;

import java.util.List;
import java.util.Optional;

public class TestDatabaseData {

    public List<Book> getTestBooks() {
        Book book1 = book1();
        book1.setReader(Optional.of(reader2()));
        Book book2 = book2();
        book2.setReader(Optional.of(reader2()));
        Book book3 = book3();
        book3.setReader(Optional.empty());
        return List.of(book1, book2, book3);
    }

    public List<Reader> getTestReaders() {
        Reader reader1 = reader1();
        reader1.setBooks(List.of());
        Reader reader2 = reader2();
        reader2.setBooks(List.of(book1(), book2()));
        Reader reader3 = reader3();
        reader3.setBooks(List.of());
        return List.of(reader1, reader2, reader3);
    }

    public Book book1() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Java. The Complete Reference. Twelfth Edition");
        book1.setAuthor("Herbert Schildt");
        return book1;
    }

    public Book book2() {
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Java. An Introduction to Problem Solving & Programming");
        book2.setAuthor("Walter Savitch");
        return book2;
    }

    public Book book3() {
        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Data Structures And Algorithms Made Easy In JAVA");
        book3.setAuthor("Narasimha Karumanchi");
        return book3;
    }

    public Reader reader1() {
        Reader reader1 = new Reader();
        reader1.setId(1L);
        reader1.setName("Zhirayr Hovik");
        return reader1;
    }

    public Reader reader2() {
        Reader reader2 = new Reader();
        reader2.setId(2L);
        reader2.setName("Voski Daniel");
        return reader2;
    }

    public Reader reader3() {
        Reader reader3 = new Reader();
        reader3.setId(3L);
        reader3.setName("Ruben Nazaret");
        return reader3;
    }

    public Book newBook() {
        Book newBook = new Book();
        newBook.setAuthor("Rebecca Serle");
        newBook.setTitle("One Italian Summer");
        newBook.setReader(Optional.empty());
        return newBook;
    }

    public Reader newReader() {
        Reader newReader = new Reader();
        newReader.setName("Addy Morra");
        return newReader;
    }

}

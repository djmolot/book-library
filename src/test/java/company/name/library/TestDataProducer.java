package company.name.library;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestDataProducer {

    public static Map<Long, Book> newAllBooksMap() {
        Map<Long, Book> allBooks = new HashMap<>();
        Book book1 = newBook1();
        book1.setReader(Optional.of(newReader2()));
        Book book2 = newBook2();
        book2.setReader(Optional.of(newReader2()));
        Book book3 = newBook3();
        book3.setReader(Optional.empty());
        allBooks.put(book1.getId(), book1);
        allBooks.put(book2.getId(), book2);
        allBooks.put(book3.getId(), book3);
        return allBooks;
    }

    public static Map<Long, Reader> newAllReadersMap() {
        Map<Long, Reader> allReaders = new HashMap<>();
        Reader reader1 = newReader1();
        reader1.setBooks(List.of());
        Reader reader2 = newReader2();
        reader2.setBooks(List.of(newBook1(), newBook2()));
        Reader reader3 = newReader3();
        reader3.setBooks(List.of());
        allReaders.put(reader1.getId(), reader1);
        allReaders.put(reader2.getId(), reader2);
        allReaders.put(reader3.getId(), reader3);
        return allReaders;
    }

    public static List<Book> newAllBooksList() {
        List<Book> allBooks = new ArrayList<>();
        Book book1 = newBook1();
        book1.setReader(Optional.of(newReader2()));
        Book book2 = newBook2();
        book2.setReader(Optional.of(newReader2()));
        Book book3 = newBook3();
        book3.setReader(Optional.empty());
        allBooks.add(book1);
        allBooks.add(book2);
        allBooks.add(book3);
        return allBooks;
    }

    public static List<Reader> newAllReadersList() {
        List<Reader> allReaders = new ArrayList<>();
        Reader reader1 = newReader1();
        reader1.setBooks(List.of());
        Reader reader2 = newReader2();
        reader2.setBooks(List.of(newBook1(), newBook2()));
        Reader reader3 = newReader3();
        reader3.setBooks(List.of());
        allReaders.add(reader1);
        allReaders.add(reader2);
        allReaders.add(reader3);
        return allReaders;
    }

    public static Book newBookForController() {
        Book newBook = new Book();
        newBook.setAuthor("Rebecca Serle");
        newBook.setTitle("One Italian Summer");
        newBook.setMaxBorrowTimeInDays(21);
        newBook.setRestricted(false);
        return newBook;
    }

    public static Book newBookForRepository() {
        Book newBook = new Book();
        newBook.setAuthor("Rebecca Serle");
        newBook.setTitle("One Italian Summer");
        newBook.setReader(Optional.empty());
        newBook.setBorrowDate(Optional.empty());
        newBook.setMaxBorrowTimeInDays(21);
        newBook.setRestricted(false);
        return newBook;
    }

    public static Reader newReader() {
        Reader newReader = new Reader();
        newReader.setName("Addy Morra");
        newReader.setBooks(new ArrayList<>());
        return newReader;
    }

    public static Book newBook1() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Java. The Complete Reference. Twelfth Edition");
        book1.setAuthor("Herbert Schildt");
        book1.setReader(Optional.empty());
        book1.setBorrowDate(Optional.empty());
        book1.setMaxBorrowTimeInDays(21);
        book1.setRestricted(false);
        return book1;
    }

    public static Book newBook2() {
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Java. An Introduction to Problem Solving & Programming");
        book2.setAuthor("Walter Savitch");
        book2.setReader(Optional.empty());
        book2.setBorrowDate(Optional.empty());
        book2.setMaxBorrowTimeInDays(21);
        book2.setRestricted(false);
        return book2;
    }

    public static Book newBook3() {
        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Data Structures And Algorithms Made Easy In JAVA");
        book3.setAuthor("Narasimha Karumanchi");
        book3.setReader(Optional.empty());
        book3.setBorrowDate(Optional.empty());
        book3.setMaxBorrowTimeInDays(21);
        book3.setRestricted(false);
        return book3;
    }

    public static Reader newReader1() {
        Reader reader1 = new Reader();
        reader1.setId(1L);
        reader1.setName("Zhirayr Hovik");
        LocalDate localDate = LocalDate.parse("2001-10-27", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        reader1.setBirthDate(localDate);
        return reader1;
    }

    public static Reader newReader2() {
        Reader reader2 = new Reader();
        reader2.setId(2L);
        reader2.setName("Voski Daniel");
        LocalDate localDate = LocalDate.parse("2003-05-18", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        reader2.setBirthDate(localDate);
        return reader2;
    }

    public static Reader newReader3() {
        Reader reader3 = new Reader();
        reader3.setId(3L);
        reader3.setName("Ruben Nazaret");
        LocalDate localDate = LocalDate.parse("2002-07-15", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        reader3.setBirthDate(localDate);
        return reader3;
    }

    public static Book bookWithInvalidTitle() {
        Book book = new Book();
        book.setTitle("Java");
        book.setAuthor("Herbert Schildt");
        book.setReader(Optional.empty());
        return book;
    }

    public static Book bookWithInvalidAuthor() {
        Book book = new Book();
        book.setTitle("Java. The Complete Reference. Twelfth Edition");
        book.setAuthor("He");
        book.setReader(Optional.empty());
        return book;
    }

    public static Reader readerWithInvalidName() {
        Reader reader = new Reader();
        reader.setName("Ad");
        return reader;
    }


}

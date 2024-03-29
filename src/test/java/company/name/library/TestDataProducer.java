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
        book1.setBorrowDate(Optional.of(LocalDate.now()));
        Book book2 = newBook2();
        book2.setReader(Optional.of(newReader2()));
        book2.setBorrowDate(Optional.of(LocalDate.now()));
        Book book3 = newBook3();
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
        Book book1 = newBook1();
        book1.setBorrowDate(Optional.of(LocalDate.now()));
        Book book2 = newBook2();
        book2.setBorrowDate(Optional.of(LocalDate.now()));
        reader2.setBooks(List.of(book1, book2));
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
        book1.setBorrowDate(Optional.of(LocalDate.now()));
        Book book2 = newBook2();
        book2.setReader(Optional.of(newReader2()));
        book2.setBorrowDate(Optional.of(LocalDate.now()));
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

    public static Book newBook() {
        Book newBook = new Book();
        newBook.setAuthor("Rebecca Serle");
        newBook.setTitle("One Italian Summer");
        return newBook;
    }

    public static Reader newReader() {
        Reader newReader = new Reader();
        newReader.setName("Addy Morra");
        newReader.setBooks(new ArrayList<>());
        LocalDate localDate = LocalDate.parse("2000-04-11", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        newReader.setBirthDate(localDate);
        return newReader;
    }

    public static Book newBook1() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Java. The Complete Reference. Twelfth Edition");
        book1.setAuthor("Herbert Schildt");
        return book1;
    }

    public static Book newBook2() {
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Java. An Introduction to Problem Solving & Programming");
        book2.setAuthor("Walter Savitch");
        book2.setMaxBorrowTimeInDays(21);
        return book2;
    }

    public static Book newBook3() {
        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Data Structures And Algorithms Made Easy In JAVA");
        book3.setAuthor("Narasimha Karumanchi");
        book3.setRestricted(true);
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
        LocalDate localDate = LocalDate.now().minusYears(15);
        reader3.setBirthDate(localDate);
        return reader3;
    }

    public static Book book2BorrowedByReader2() {
        Book book2 = newBook2();
        book2.setReader(Optional.of(newReader2()));
        book2.setBorrowDate(Optional.of(LocalDate.now()));
        return book2;
    }

    public static Book newRestrictedBook() {
        Book book3 = new Book();
        book3.setId(19L);
        book3.setTitle("The World's Wife");
        book3.setAuthor("Carol Ann Duffy");
        book3.setRestricted(true);
        return book3;
    }

    public static Reader newReaderUnder18() {
        Reader reader7 = new Reader();
        reader7.setId(7L);
        reader7.setName("Barbara J. Wise");
        LocalDate localDate = LocalDate.now().minusYears(15);
        reader7.setBirthDate(localDate);
        return reader7;
    }


    public static Book bookWithInvalidTitle() {
        Book book = new Book();
        book.setTitle("Java");
        book.setAuthor("Herbert Schildt");
        book.setMaxBorrowTimeInDays(21);
        book.setRestricted(false);
        return book;
    }

    public static Book bookWithInvalidAuthor() {
        Book book = new Book();
        book.setTitle("Java. The Complete Reference. Twelfth Edition");
        book.setAuthor("He");
        book.setMaxBorrowTimeInDays(21);
        book.setRestricted(false);
        return book;
    }

    public static Reader readerWithInvalidName() {
        Reader reader = new Reader();
        reader.setName("Ad");
        LocalDate localDate = LocalDate.parse("2004-11-22", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        reader.setBirthDate(localDate);
        return reader;
    }

    public static List<Book> generateBooksListOfReader(Reader reader, int listSize) {
        List<Book> readersBooks = new ArrayList<>();
        for (int i = 1; i <= listSize ; i++) {
            Book book = newBook2();
            book.setReader(Optional.of(reader));
            book.setBorrowDate(Optional.of(LocalDate.now()));
            readersBooks.add(book);
        }
        return readersBooks;
    }

    public static Book newExpiredBookOfReader(Reader reader, int maxBorrowTimeInDays, int daysExpired) {
        Book expiredBook = newBook2();
        expiredBook.setReader(Optional.of(reader));
        expiredBook.setMaxBorrowTimeInDays(maxBorrowTimeInDays);
        int holdingPeriod = maxBorrowTimeInDays + daysExpired;
        expiredBook.setBorrowDate(Optional.of(LocalDate.now().minusDays(holdingPeriod)));
        return expiredBook;
    }

}

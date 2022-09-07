package company.name.service;

import company.name.dao.BookDao;
import company.name.dao.BookDaoStorageImpl;
import company.name.dao.LibraryDao;
import company.name.dao.LibraryDaoImpl;
import company.name.dao.ReaderDao;
import company.name.dao.ReaderDaoStorageImpl;
import company.name.models.Book;
import company.name.models.Reader;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LibraryServiceImpl implements LibraryService {
    private final LibraryDao libraryDao = new LibraryDaoImpl();
    private final BookDao bookDao = new BookDaoStorageImpl();
    private final ReaderDao readerDao = new ReaderDaoStorageImpl();
    private static final Scanner scanner = new Scanner(System.in);


    @Override
    public void borrowBookForReader(Reader reader, Book book) {
        libraryDao.borrowBookForReader(reader, book);
    }

    @Override
    public void returnBookFromReader(Reader reader, Book book) {
        libraryDao.returnBookFromReader(reader, book);
    }

    @Override
    public Reader getCurrentReaderOfBook(Book book) {
        return readerDao.getCurrentReaderOfBook(book);
    }

    @Override
    public List<Book> getAllBooksByReader(Reader reader) {
        return readerDao.getBorrowedBooksIds(reader).stream()
                .map(id -> bookDao.get(id))
                .collect(Collectors.toList());
    }

    @Override
    public void showAllBooks() {
        bookDao.getAll().forEach(System.out::println);
    }

    @Override
    public void showAllReaders() {
        readerDao.getAll().forEach(System.out::println);
    }

    @Override
    public void registerNewReader() {
        System.out.println("Please enter new reader full name!");
        var readerName = scanner.nextLine();
        Reader reader = new Reader();
        reader.setName(readerName);
        readerDao.add(reader);
    }

    @Override
    public void addNewBook() {
        System.out.println("Please enter new book name and author separated by “/”. Like this: name/author");
        var bookInput = scanner.nextLine();
        String[] splittedInput = bookInput.split("/");
        Book book = new Book();
        book.setName(splittedInput[0]);
        book.setAuthor(splittedInput[1]);
        bookDao.add(book);
    }

    @Override
    public void borrowBookToReader() {
        System.out.println("Please enter book ID and reader ID separated by “/”. Like this: 1/2");
        var input = scanner.nextLine();
        String[] splittedInput = input.split("/");
        Long bookId = Long.parseLong(splittedInput[0]);
        Long readerId = Long.parseLong(splittedInput[1]);
        Book book = bookDao.get(bookId);
        Reader reader = readerDao.get(readerId);
        borrowBookForReader(reader, book);
    }

    @Override
    public void returnBookToLibrary() {
        System.out.println("Please enter book ID");
        var input = scanner.nextLine();
        Long bookId = Long.parseLong(input);
        Reader reader = getCurrentReaderOfBook(bookDao.get(bookId));
        Book book = bookDao.get(bookId);
        returnBookFromReader(reader, book);
    }

    @Override
    public void showAllBooksByReader() {
        System.out.println("Please enter reader ID");
        var input = scanner.nextLine();
        Long readerId = Long.parseLong(input);
        getAllBooksByReader(readerDao.get(readerId))
                .forEach(System.out::println);
    }

    @Override
    public void showReaderOfBook() {
        System.out.println("Please enter book ID");
        var input = scanner.nextLine();
        Long bookId = Long.parseLong(input);
        Reader reader = getCurrentReaderOfBook(bookDao.get(bookId));
        System.out.println(reader);
    }

    @Override
    public void prepareLibraryData() {
        Book book1 = new Book();
        book1.setAuthor("Herbert Schildt");
        book1.setName("Java. The Complete Reference. Twelfth Edition");
        bookDao.add(book1);

        Book book2 = new Book();
        book2.setAuthor("Walter Savitch");
        book2.setName("Java. An Introduction to Problem Solving & Programming");
        bookDao.add(book2);

        Book book3 = new Book();
        book3.setAuthor("Narasimha Karumanchi");
        book3.setName("Data Structures And Algorithms Made Easy In JAVA");
        bookDao.add(book3);

        Reader reader1 = new Reader();
        reader1.setName("Zhirayr Hovik");
        readerDao.add(reader1);

        Reader reader2 = new Reader();
        reader2.setName("Voski Daniel");
        readerDao.add(reader2);

        Reader reader3 = new Reader();
        reader3.setName("Ruben Nazaret");
        readerDao.add(reader3);
    }

}

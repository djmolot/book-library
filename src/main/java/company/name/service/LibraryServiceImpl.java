package company.name.service;

import company.name.dao.BookDao;
import company.name.dao.BookDaoStorageImpl;
import company.name.dao.LibraryDao;
import company.name.dao.LibraryDaoImpl;
import company.name.dao.ReaderDao;
import company.name.dao.ReaderDaoStorageImpl;
import company.name.exceptions.NoEntityWithSuchIdException;
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
        try {
            borrowBookForReader(bookId, readerId);
        } catch (NoEntityWithSuchIdException e) {
            System.out.println(e.getMessage());;
        }
    }

    @Override
    public void returnBookToLibrary() {
        System.out.println("Please enter book ID");
        var input = scanner.nextLine();
        Long bookId = Long.parseLong(input);
        try {
            Long readerId = getReaderIdByBookId(bookId);
            returnBookFromReader(bookId, readerId);
        } catch (NoEntityWithSuchIdException e) {
            System.out.println(e.getMessage());;
        }
    }

    @Override
    public void showAllBooksOfReader() {
        System.out.println("Please enter reader ID");
        var input = scanner.nextLine();
        Long readerId = Long.parseLong(input);
        try {
            getAllBooksByReader(readerId).forEach(System.out::println);
        } catch (NoEntityWithSuchIdException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void showReaderOfBook() {
        System.out.println("Please enter book ID");
        var input = scanner.nextLine();
        Long bookId = Long.parseLong(input);
        try {
            Long readerId = getReaderIdByBookId(bookId);
            readerDao.getById(readerId)
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.err.println("Reader with id " + readerId + " does not exists in the Library")
                    );
        } catch (NoEntityWithSuchIdException e) {
            System.out.println(e.getMessage());
        }
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

    private void borrowBookForReader(Long bookId, Long readerId)
            throws NoEntityWithSuchIdException {
        if(!readerDao.containsReaderWithId(readerId)) {
            throw new NoEntityWithSuchIdException(
                    "Reader with specified id " + readerId + " does not exists in the storage");
        }
        if(!bookDao.containsBookWithId(bookId)) {
            throw new NoEntityWithSuchIdException(
                    "Book with specified id " + bookId + " does not exists in the storage");
        }
        libraryDao.borrowBookForReader(bookId, readerId);
    }

    private void returnBookFromReader(Long bookId, Long readerId)
            throws NoEntityWithSuchIdException {
        if(!readerDao.containsReaderWithId(readerId)) {
            throw new NoEntityWithSuchIdException(
                    "Reader with specified id " + readerId + " does not exists in the storage");
        }
        if(!bookDao.containsBookWithId(bookId)) {
            throw new NoEntityWithSuchIdException(
                    "Book with specified id " + bookId + " does not exists in the storage");
        }
        libraryDao.returnBookFromReader(bookId, readerId);
    }

    private Long getReaderIdByBookId(Long bookId) throws NoEntityWithSuchIdException {
        if(!bookDao.containsBookWithId(bookId)) {
            throw new NoEntityWithSuchIdException(
                    "Book with specified id " + bookId + " does not exists in the storage");
        }
        return libraryDao.getReaderIdByBookId(bookId);
    }

    private List<Book> getAllBooksByReader(Long readerId) throws NoEntityWithSuchIdException {
        if(!readerDao.containsReaderWithId(readerId)) {
            throw new NoEntityWithSuchIdException(
                    "Reader with specified id " + readerId + " does not exists in the storage");
        }
        return libraryDao.getBooksIdsByReaderId(readerId).stream()
                .map(id -> bookDao.getById(id).get())
                .collect(Collectors.toList());
    }
}

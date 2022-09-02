package company.name;

import company.name.dao.BookDao;
import company.name.dao.BookDaoStorageImpl;
import company.name.dao.LibraryDao;
import company.name.dao.LibraryDaoImpl;
import company.name.dao.ReaderDao;
import company.name.dao.ReaderDaoStorageImpl;
import company.name.models.Book;
import company.name.models.Reader;
import company.name.service.BookService;
import company.name.service.BookServiceImpl;
import company.name.service.LibraryService;
import company.name.service.LibraryServiceImpl;
import company.name.service.ReaderService;
import company.name.service.ReaderServiceImpl;
import java.util.Scanner;

public class Main {
    private static final BookDao bookDao = new BookDaoStorageImpl();
    private static final BookService bookService = new BookServiceImpl(bookDao);
    private static final ReaderDao readerDao = new ReaderDaoStorageImpl();
    private static final ReaderService readerService = new ReaderServiceImpl(readerDao);
    private static final LibraryDao libraryDao = new LibraryDaoImpl(bookDao, readerDao);
    private static final LibraryService libraryService = new LibraryServiceImpl(libraryDao);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        prepareLibraryData();

        while(true) {
            printLibraryMenu();
            String command = scanner.nextLine();
            if(command.equals("1")) {
                doMenu1Handler();
            } else if(command.equals("2")) {
                doMenu2Handler();
            } else if(command.equals("3")) {
                doMenu3Handler();
            } else if(command.equals("4")) {
                doMenu4Handler();
            } else if(command.equals("5")) {
                doMenu5Handler();
            } else if(command.equals("6")) {
                doMenu6Handler();
            } else if(command.equals("7")) {
                doMenu7Handler();
            } else if(command.equals("8")) {
                doMenu8Handler();
            } else if(command.toUpperCase().equals("EXIT")) {
                doMenuExitHandler();
            } else {
                System.out.println("Unknown command. Please try again.");
            }
        }
    }

    private static void prepareLibraryData() {
        Book book1 = new Book();
        book1.setAuthor("Herbert Schildt");
        book1.setName("Java. The Complete Reference. Twelfth Edition");
        bookService.createNewBook(book1);

        Book book2 = new Book();
        book2.setAuthor("Walter Savitch");
        book2.setName("Java. An Introduction to Problem Solving & Programming");
        bookService.createNewBook(book2);

        Book book3 = new Book();
        book3.setAuthor("Narasimha Karumanchi");
        book3.setName("Data Structures And Algorithms Made Easy In JAVA");
        bookService.createNewBook(book3);

        Reader reader1 = new Reader();
        reader1.setName("Zhirayr Hovik");
        readerService.createNewReader(reader1);

        Reader reader2 = new Reader();
        reader2.setName("Voski Daniel");
        readerService.createNewReader(reader2);

        Reader reader3 = new Reader();
        reader3.setName("Ruben Nazaret");
        readerService.createNewReader(reader3);
    }

    private static void printLibraryMenu() {
        String lineDelimiter =
                "-----------------------------------------------------------------------------";

        var greetingMessage = """
        WELCOME TO THE LIBRARY!
        PLEASE, SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER AND PRESSING ENTER KEY:
        [1]SHOW ALL BOOKS IN THE LIBRARY
        [2]SHOW ALL READERS REGISTERED IN THE LIBRARY
        [3]REGISTER NEW READER
        [4]ADD NEW BOOK
        [5]BORROW A BOOK TO A READER
        [6]RETURN A BOOK TO THE LIBRARY
        [7]SHOW ALL BORROWED BOOK BY USER ID
        [8]SHOW CURRENT READER OF A BOOK WITH ID
        TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!
        """;

        System.out.println(lineDelimiter);
        System.out.print(greetingMessage);
        System.out.println(lineDelimiter);
    }

    private static void doMenu1Handler() {
        bookService.getAll().forEach(System.out::println);
    }

    private static void doMenu2Handler() {
        readerService.getAll().forEach(System.out::println);
    }

    private static void doMenu3Handler() {
        System.out.println("Please enter new reader full name!");
        String readerName = scanner.nextLine();
        Reader reader = new Reader();
        reader.setName(readerName);
        readerService.createNewReader(reader);
    }

    private static void doMenu4Handler() {
        System.out.println("Please enter new book name and author separated by “/”. Like this: name/author");
        String bookInput = scanner.nextLine();
        String[] splittedInput = bookInput.split("/");
        Book book = new Book();
        book.setName(splittedInput[0]);
        book.setAuthor(splittedInput[1]);
        bookService.createNewBook(book);
    }

    private static void doMenu5Handler() {
        System.out.println("Please enter book ID and reader ID separated by “/”. Like this: 1/2");
        String input = scanner.nextLine();
        String[] splittedInput = input.split("/");
        Long bookId = Long.parseLong(splittedInput[0]);
        Long readerId = Long.parseLong(splittedInput[1]);
        Book book = bookService.get(bookId);
        Reader reader = readerService.get(readerId);
        libraryService.borrowBookForReader(reader, book);
    }

    private static void doMenu6Handler() {
        System.out.println("Please enter book ID");
        String input = scanner.nextLine();
        Long bookId = Long.parseLong(input);
        Reader reader = libraryService.getCurrentReaderOfBook(bookService.get(bookId));
        Book book = bookService.get(bookId);
        libraryService.returnBookFromReader(reader, book);
    }

    private static void doMenu7Handler() {
        System.out.println("Please enter reader ID");
        String input = scanner.nextLine();
        Long readerId = Long.parseLong(input);
        libraryService.getAllBooksByReader(readerService.get(readerId))
                .forEach(System.out::println);
    }

    private static void doMenu8Handler() {
        System.out.println("Please enter book ID");
        String input = scanner.nextLine();
        Long bookId = Long.parseLong(input);
        Reader reader = libraryService.getCurrentReaderOfBook(bookService.get(bookId));
        System.out.println(reader);
    }

    private static void doMenuExitHandler() {
        System.exit(0);
    }

}

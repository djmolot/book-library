package company.name;

import company.name.dao.BookDao;
import company.name.dao.BookDaoStorageImpl;
import company.name.dao.ReaderDao;
import company.name.dao.ReaderDaoStorageImpl;
import company.name.models.Book;
import company.name.models.Reader;
import company.name.service.BookService;
import company.name.service.BookServiceImpl;
import company.name.service.ReaderService;
import company.name.service.ReaderServiceImpl;
import java.util.Scanner;

public class Main {
    private static final BookDao bookDao = new BookDaoStorageImpl();
    private static final BookService bookService = new BookServiceImpl(bookDao);
    private static final ReaderDao readerDao = new ReaderDaoStorageImpl();
    private static final ReaderService readerService = new ReaderServiceImpl(readerDao);

    public static void main(String[] args) {

        prepareLibraryData();

        Scanner scanner = new Scanner(System.in);
        while(true) {
            printLibraryMenu();
            String command = scanner.nextLine();
            if(command.equals("1")) {
                bookService.getAll().forEach(System.out::println);
            } else if(command.equals("2")) {
                readerService.getAll().forEach(System.out::println);
            } else if(command.toUpperCase().equals("EXIT")) {
                System.exit(0);
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

    public static void printLibraryMenu() {
        String lineDelimiter =
                "-----------------------------------------------------------------------------";

        var greetingMessage = """
        WELCOME TO THE LIBRARY!
        PLEASE, SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER AND PRESSING ENTER KEY:
        [1]SHOW ALL BOOKS IN THE LIBRARY
        [2]SHOW ALL READERS REGISTERED IN THE LIBRARY
        TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!
        """;

        System.out.println(lineDelimiter);
        System.out.print(greetingMessage);
        System.out.println(lineDelimiter);
    }
}

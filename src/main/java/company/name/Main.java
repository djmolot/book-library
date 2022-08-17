package company.name;

import company.name.dao.BookDao;
import company.name.dao.BookDaoImpl;
import company.name.dao.ReaderDao;
import company.name.dao.ReaderDaoImpl;
import company.name.models.Book;
import company.name.models.Reader;
import company.name.service.BookService;
import company.name.service.BookServiceImpl;
import company.name.service.ReaderService;
import company.name.service.ReaderServiceImpl;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String LINE_DELIMITER =
            "-----------------------------------------------------------------------------";

    public static void main(String[] args) {
        BookDao bookDao = new BookDaoImpl();
        BookService bookService = new BookServiceImpl(bookDao);
        bookService.createNewBook("Java. The Complete Reference. Twelfth Edition",
                "Herbert Schildt");
        bookService.createNewBook("Java. An Introduction to Problem Solving & Programming",
                "Walter Savitch");
        bookService.createNewBook("Data Structures And Algorithms Made Easy In JAVA",
                "Narasimha Karumanchi");

        ReaderDao readerDao = new ReaderDaoImpl();
        ReaderService readerService = new ReaderServiceImpl(readerDao);
        readerService.createNewReader("Zhirayr Hovik");
        readerService.createNewReader("Voski Daniel");
        readerService.createNewReader("Ruben Nazaret");

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println(LINE_DELIMITER);
            System.out.println("WELCOME TO THE LIBRARY!");
            System.out.println("");
            System.out.println("PLEASE,SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER AND PRESSING ENTER KEY:");
            System.out.println("[1]SHOW ALL BOOKS IN THE LIBRARY");
            System.out.println("[2]SHOW ALL READERS REGISTERED IN THE LIBRARY");
            System.out.println("");
            System.out.println("TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!");
            System.out.println(LINE_DELIMITER);

            String command = scanner.nextLine();
            if(command.equals("1")) {
                List<Book> allBooks = bookService.getAll();
                for (Book book : allBooks) {
                    System.out.println(book.getId() + ". " + book.getAuthor() + " \"" + book.getName() + ".\"");
                }
            } else if(command.equals("2")) {
                List<Reader> allReaders = readerService.getAll();
                for (Reader reader : allReaders) {
                    System.out.println(reader.getId() + ". " + reader.getName());
                }
            } else if(command.toUpperCase().equals("EXIT")) {
                System.exit(0);
            } else {
                System.out.println("Unknown command. Please try again.");
            }
        }
    }
}

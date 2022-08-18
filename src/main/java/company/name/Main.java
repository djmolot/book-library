package company.name;

import company.name.dao.BookDao;
import company.name.dao.BookDaoStorageImpl;
import company.name.dao.ReaderDao;
import company.name.dao.ReaderDaoImpl;
import company.name.models.dto.BookRequestDto;
import company.name.models.dto.ReaderRequestDto;
import company.name.service.BookService;
import company.name.service.BookServiceImpl;
import company.name.service.ReaderService;
import company.name.service.ReaderServiceImpl;
import company.name.service.mapper.BookMapper;
import company.name.service.mapper.ReaderMapper;

import java.util.Scanner;

public class Main {
    private static final String LINE_DELIMITER =
            "-----------------------------------------------------------------------------";

    public static void main(String[] args) {
        BookDao bookDao = new BookDaoStorageImpl();
        BookMapper bookMapper = new BookMapper();
        BookService bookService = new BookServiceImpl(bookDao, bookMapper);

        BookRequestDto requestDtoB1 = new BookRequestDto();
        requestDtoB1.setAuthor("Herbert Schildt");
        requestDtoB1.setName("Java. The Complete Reference. Twelfth Edition");
        bookService.createNewBook(requestDtoB1);

        BookRequestDto requestDtoB2 = new BookRequestDto();
        requestDtoB2.setAuthor("Walter Savitch");
        requestDtoB2.setName("Java. An Introduction to Problem Solving & Programming");
        bookService.createNewBook(requestDtoB2);

        BookRequestDto requestDtoB3 = new BookRequestDto();
        requestDtoB3.setAuthor("Narasimha Karumanchi");
        requestDtoB3.setName("Data Structures And Algorithms Made Easy In JAVA");
        bookService.createNewBook(requestDtoB3);

        ReaderDao readerDao = new ReaderDaoImpl();
        ReaderMapper readerMapper = new ReaderMapper();
        ReaderService readerService = new ReaderServiceImpl(readerDao, readerMapper);

        ReaderRequestDto requestDtoR1 = new ReaderRequestDto();
        requestDtoR1.setName("Zhirayr Hovik");
        readerService.createNewReader(requestDtoR1);

        ReaderRequestDto requestDtoR2 = new ReaderRequestDto();
        requestDtoR2.setName("Voski Daniel");
        readerService.createNewReader(requestDtoR2);

        ReaderRequestDto requestDtoR3 = new ReaderRequestDto();
        requestDtoR3.setName("Ruben Nazaret");
        readerService.createNewReader(requestDtoR3);

        Scanner scanner = new Scanner(System.in);
        while(true) {
            var greetingMessage =
                """
                WELCOME TO THE LIBRARY!
                PLEASE, SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER AND PRESSING ENTER KEY:
                [1]SHOW ALL BOOKS IN THE LIBRARY
                [2]SHOW ALL READERS REGISTERED IN THE LIBRARY
                TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!
                """;
            System.out.println(LINE_DELIMITER);
            System.out.print(greetingMessage);
            System.out.println(LINE_DELIMITER);

            String command = scanner.nextLine();
            if(command.equals("1")) {
                bookService.getAll().stream()
                        .map(book -> book.getId() + ". " + book.getAuthor() + ". \"" + book.getName() + ".\"")
                        .forEach(System.out::println);
            } else if(command.equals("2")) {
                readerService.getAll().stream()
                        .map(reader -> reader.getId() + ". " + reader.getName())
                        .forEach(System.out::println);
            } else if(command.toUpperCase().equals("EXIT")) {
                System.exit(0);
            } else {
                System.out.println("Unknown command. Please try again.");
            }
        }
    }
}

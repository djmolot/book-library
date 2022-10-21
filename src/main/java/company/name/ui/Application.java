package company.name.ui;

import company.name.exceptions.DaoLayerException;
import company.name.exceptions.ServiceLayerException;
import company.name.models.Book;
import company.name.models.Reader;
import company.name.service.LibraryService;
import company.name.service.LibraryServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Application {
    private static final LibraryService libraryService = new LibraryServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public void run() {
        prepareLibraryData();
        while (true) {
            printLibraryMenu();
            var command = scanner.nextLine();
            switch (command) {
                case "1" -> showAllBooks();
                case "2" -> showAllReaders();
                case "3" -> registerNewReader();
                case "4" -> addNewBook();
                case "5" -> borrowBookToReader();
                case "6" -> returnBookToLibrary();
                case "7" -> showAllBooksOfReader();
                case "8" -> showReaderOfBook();
                case "exit" -> System.exit(0);
                default -> System.out.println("Unknown command. Please try again.");
            }
        }
    }

    private void prepareLibraryData() {
        try {
            libraryService.prepareLibraryData();
        } catch (DaoLayerException e) {
            System.err.println("Error on DAO layer during preparing Library Data" + e.getMessage());
        }
    }

    private void printLibraryMenu() {
        var lineDelimiter =
                "-----------------------------------------------------------------------------";

        var greetingMessage = """
        WELCOME TO THE LIBRARY!
        PLEASE, SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER
        AND PRESSING ENTER KEY:
        [1]SHOW ALL BOOKS IN THE LIBRARY
        [2]SHOW ALL READERS REGISTERED IN THE LIBRARY
        [3]REGISTER NEW READER
        [4]ADD NEW BOOK
        [5]BORROW A BOOK TO A READER
        [6]RETURN A BOOK TO THE LIBRARY
        [7]SHOW ALL BORROWED BOOKS BY READER ID
        [8]SHOW CURRENT READER OF A BOOK WITH ID
        TYPE “exit” TO STOP THE PROGRAM AND EXIT!
                """;

        System.out.println(lineDelimiter);
        System.out.print(greetingMessage);
        System.out.println(lineDelimiter);
    }

    private void showAllBooks() {
        try {
            libraryService.getAllBooks().forEach(book -> {
                System.out.println(book);
                book.getReader().ifPresentOrElse(
                        reader -> System.out.println(" borowed by reader " + reader),
                        () -> System.out.println(" available")
                );
            });
        } catch (DaoLayerException e) {
            System.err.println("Can't get all books from DB due to error on DAO layer. "
                    + e.getMessage());
        }
    }

    private void showAllReaders() {
        try {
            libraryService.getAllReaders().forEach(reader -> {
                System.out.println(reader);
                if (!reader.getBooks().isEmpty()) {
                    System.out.println("     Borrowed books: ");
                    reader.getBooks().forEach(
                            book -> System.out.println("     " + book));
                } else {
                    System.out.println("     Has not borrowed books");
                }
            });
        } catch (DaoLayerException e) {
            System.err.println("Can't get all readers from DB due to error on DAO layer. "
                    + e.getMessage());
        }
    }

    private void registerNewReader() {
        System.out.println("Please enter new reader full name.");
        String input = scanner.nextLine();
        try {
            Reader newReader = libraryService.registerNewReader(input);
            System.out.println("New reader was successfully created " + newReader);
        } catch (ServiceLayerException e) {
            System.err.println("Can't register new reader due to error on service layer. "
                    + e.getMessage());
        } catch (DaoLayerException e) {
            System.err.println("Can't register new reader due to error on DAO layer. "
                    + e.getMessage());
        }
    }

    private void addNewBook() {
        System.out.println(
                "Please enter new book title and author separated by “/”. Like this: title/author");
        String input = scanner.nextLine();
        try {
            Book newBook = libraryService.addNewBook(input);
            System.out.println("New book was successfully created " + newBook);
        } catch (ServiceLayerException e) {
            System.err.println("Can't add new book due to error on service layer. "
                    + e.getMessage());
        } catch (DaoLayerException e) {
            System.err.println("Can't add new book due to error on DAO layer. " + e.getMessage());
        }
    }

    private void borrowBookToReader() {
        System.out.println("Please enter book ID and reader ID separated by “/”. Like this: 1/2");
        var input = scanner.nextLine();
        try {
            libraryService.borrowBookToReader(input);
            System.out.println("Book was successfully borrowed to reader.");
        } catch (ServiceLayerException e) {
            System.err.println("Can't borrow a book to a reader due to error on Service layer. "
                    + e.getMessage());
        } catch (DaoLayerException e) {
            System.err.println("Can't borrow a book to a reader due to error on DAO layer. "
                    + e.getMessage());
        }
    }

    private void returnBookToLibrary() {
        System.out.println("Please enter book ID");
        var input = scanner.nextLine();
        try {
            libraryService.returnBookToLibrary(input);
            System.out.println("Book was successfully returned to the library");
        } catch (ServiceLayerException e) {
            System.err.println("Can't return a book from a reader due to error on Service layer. "
                    + e.getMessage());
        } catch (DaoLayerException e) {
            System.err.println("Can't return a book from a reader due to error on DAO layer. "
                    + e.getMessage());
        }
    }

    private void showAllBooksOfReader() {
        System.out.println("Please enter reader ID");
        var input = scanner.nextLine();
        try {
            List<Book> allBooksOfReader = libraryService.getAllBooksOfReader(input);
            if (allBooksOfReader.isEmpty()) {
                System.out.println("Reader has not borowed books.");
            } else {
                System.out.println("Books of reader:");
                allBooksOfReader.forEach(System.out::println);
            }
        } catch (ServiceLayerException e) {
            System.err.println("Can't get all books of reader due to error on service layer. "
                    + e.getMessage());
        } catch (DaoLayerException e) {
            System.err.println("Can't get all books of reader due to error on DAO layer. "
                    + e.getMessage());
        }
    }

    public void showReaderOfBook() {
        System.out.println("Please enter book ID");
        var input = scanner.nextLine();
        try {
            Optional<Reader> optional = libraryService.getReaderOfBookWithId(input);
            if (optional.isPresent()) {
                System.out.println("Reader of this book is " + optional.get());
            } else {
                System.out.println("Book is not borrowed. No reader to show");
            }
        } catch (ServiceLayerException e) {
            System.err.println("Can't get reader of book due to error on service layer. "
                    + e.getMessage());
        } catch (DaoLayerException e) {
            System.err.println("Can't get reader of book due to error on DAO layer. "
                    + e.getMessage());
        }
    }
}

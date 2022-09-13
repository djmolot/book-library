package company.name;

import company.name.service.LibraryService;
import company.name.service.LibraryServiceImpl;
import java.util.Scanner;

public class Main {
    private static final LibraryService libraryService = new LibraryServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {

        libraryService.prepareLibraryData();

        while(true) {
            printLibraryMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1" -> libraryService.showAllBooks();
                case "2" -> libraryService.showAllReaders();
                case "3" -> libraryService.registerNewReader();
                case "4" -> libraryService.addNewBook();
                case "5" -> libraryService.borrowBookToReader();
                case "6" -> libraryService.returnBookToLibrary();
                case "7" -> libraryService.showAllBooksOfReader();
                case "8" -> libraryService.showReaderOfBook();
                case "exit" -> System.exit(0);
                default -> System.out.println("Unknown command. Please try again.");
            }
        }
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
        [7]SHOW ALL BORROWED BOOKS BY USER ID
        [8]SHOW CURRENT READER OF A BOOK WITH ID
        TYPE “exit” TO STOP THE PROGRAM AND EXIT!
        """;

        System.out.println(lineDelimiter);
        System.out.print(greetingMessage);
        System.out.println(lineDelimiter);
    }

}

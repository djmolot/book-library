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
            if(command.equals("1")) {
                libraryService.doMenu1Handler();
            } else if(command.equals("2")) {
                libraryService.doMenu2Handler();
            } else if(command.equals("3")) {
                System.out.println("Please enter new reader full name!");
                String readerName = scanner.nextLine();
                libraryService.doMenu3Handler(readerName);
            } else if(command.equals("4")) {
                System.out.println("Please enter new book name and author separated by “/”. Like this: name/author");
                String bookInput = scanner.nextLine();
                libraryService.doMenu4Handler(bookInput);
            } else if(command.equals("5")) {
                System.out.println("Please enter book ID and reader ID separated by “/”. Like this: 1/2");
                String input = scanner.nextLine();
                libraryService.doMenu5Handler(input);
            } else if(command.equals("6")) {
                System.out.println("Please enter book ID");
                String input = scanner.nextLine();
                libraryService.doMenu6Handler(input);
            } else if(command.equals("7")) {
                System.out.println("Please enter reader ID");
                String input = scanner.nextLine();
                libraryService.doMenu7Handler(input);
            } else if(command.equals("8")) {
                System.out.println("Please enter book ID");
                String input = scanner.nextLine();
                libraryService.doMenu8Handler(input);
            } else if(command.toUpperCase().equals("EXIT")) {
                System.exit(0);
            } else {
                System.out.println("Unknown command. Please try again.");
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
        [7]SHOW ALL BORROWED BOOK BY USER ID
        [8]SHOW CURRENT READER OF A BOOK WITH ID
        TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!
        """;

        System.out.println(lineDelimiter);
        System.out.print(greetingMessage);
        System.out.println(lineDelimiter);
    }

}

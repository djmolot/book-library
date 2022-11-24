package company.name.service;

import company.name.dao.BookDao;
import company.name.dao.ReaderDao;
import company.name.entities.Book;
import company.name.entities.Reader;
import company.name.exceptions.ServiceLayerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class LibraryServiceImplTest {
    private final BookDao bookDaoMock = Mockito.mock(BookDao.class);
    private final ReaderDao readerDaoMock = Mockito.mock(ReaderDao.class);
    private final LibraryService libraryService = new LibraryServiceImpl(bookDaoMock, readerDaoMock);

    @Test
    @DisplayName("Should successfully create new reader")
    void registerNewReader_ok() {
        String input = "Rudolf Mann";

        Reader readerToPass = new Reader();
        readerToPass.setName("Rudolf Mann");

        Reader readerToReturn = new Reader();
        readerToReturn.setId(444L);
        readerToReturn.setName("Rudolf Mann");

        Mockito.when(readerDaoMock.add(readerToPass)).thenReturn(readerToReturn);
        Reader createdReader = libraryService.registerNewReader(input);
        assertAll(
                () -> assertNotNull(createdReader, "method must not return instead of added into DB reader null"),
                () -> assertNotNull(createdReader.getId(), "id of added into DB reader can't be null"),
                () -> assertEquals(444L, createdReader.getId(), "libraryService.registerNewReader(input) returned reader with wrong id"),
                () -> assertEquals("Rudolf Mann", createdReader.getName(), "libraryService.registerNewReader(input) returned reader with wrong name")
        );
    }

    @ParameterizedTest(name = "case #{index}: invalid user input [{0}]")
    @DisplayName("Should fail create new reader with invalid user input")
    @MethodSource("registerNewReader_not_ok_argumentsProvider")
    void registerNewReader_not_ok(String input, String expectedMessage) {
        var ex = assertThrows(ServiceLayerException.class,
                () -> libraryService.registerNewReader(input),
                "libraryService.registerNewReader(input) must throw ServiceLayerException when input is wrong");
        assertEquals(expectedMessage, ex.getMessage(), "actual message in exception object is wrong");
    }
    static Stream<Arguments> registerNewReader_not_ok_argumentsProvider() {
        return Stream.of(
                arguments(null, "Reader name can't be null or empty."),
                arguments("", "Reader name can't be null or empty."),
                arguments("Rudolf/Mann", "Reader name can't contain symbol '/'.")
        );
    }

    @Test
    @DisplayName("Should successfully create new book")
    void addNewBook_ok() {
        String input = "Data Structures and Abstractions with Java/Frank M. Carrano, Timothy M. Henry";

        Book bookToPass = new Book();
        bookToPass.setAuthor("Frank M. Carrano, Timothy M. Henry");
        bookToPass.setTitle("Data Structures and Abstractions with Java");

        Book bookToReturn = new Book();
        bookToReturn.setId(555L);
        bookToReturn.setAuthor("Frank M. Carrano, Timothy M. Henry");
        bookToReturn.setTitle("Data Structures and Abstractions with Java");

        Mockito.when(bookDaoMock.add(bookToPass)).thenReturn(bookToReturn);
        Book createdBook = libraryService.addNewBook(input);
        assertAll(
                () -> assertNotNull(createdBook, "libraryService.addNewBook(input) returned null"),
                () -> assertEquals(555L, createdBook.getId(), "libraryService.addNewBook(input) returned book with wrong id"),
                () -> assertEquals("Frank M. Carrano, Timothy M. Henry", createdBook.getAuthor(), "libraryService.addNewBook(input) returned book with wrong author"),
                () -> assertEquals("Data Structures and Abstractions with Java", createdBook.getTitle(), "libraryService.addNewBook(input) returned book with wrong title")
        );
    }

    @ParameterizedTest(name = "case #{index}: invalid book input [{0}]")
    @DisplayName("Should fail create new book with invalid user input")
    @MethodSource("addNewBook_not_ok_argumentsProvider")
    void addNewBook_not_ok(String input, String expectedMessage) {
        var ex = assertThrows(ServiceLayerException.class,
                () -> libraryService.addNewBook(input),
                "libraryService.addNewBook(input) must throw ServiceLayerException when input is wrong");
        assertEquals(expectedMessage, ex.getMessage(), "actual message in exception object is wrong");
    }
    static Stream<Arguments> addNewBook_not_ok_argumentsProvider() {
        return Stream.of(
                arguments(null, "New book input can't be null and should contain at least 1 symbol for both name and author."),
                arguments("", "New book input can't be null and should contain at least 1 symbol for both name and author."),
                arguments("/", "New book input can't be null and should contain at least 1 symbol for both name and author."),
                arguments("A/", "New book input can't be null and should contain at least 1 symbol for both name and author."),
                arguments("/B", "New book input can't be null and should contain at least 1 symbol for both name and author."),
                arguments("Data Structures and Abstractions with Java//Frank M. Carrano, Timothy M. Henry",
                        "New book input must contain one '/' character."),
                arguments("/Frank M. Carrano, Timothy M. Henry",
                        "New book input is not valid."),
                arguments("Data Structures and Abstractions with Java/",
                        "New book input is not valid."),
                arguments(" /Frank M. Carrano, Timothy M. Henry",
                        "New book input is not valid."),
                arguments("Data Structures and Abstractions with Java/ ",
                        "New book input is not valid.")
                );
    }

    @Test
    @DisplayName("Should successfully borrow book to reader")
    void borrowBookToReader_ok() {
        String input = "1/2";
        Reader readerToReturn = new Reader();
        readerToReturn.setId(2L);
        Book bookToReturn = new Book();
        bookToReturn.setId(1L);
        bookToReturn.setReader(Optional.empty());
        Mockito.when(readerDaoMock.getById(2L)).thenReturn(Optional.of(readerToReturn));
        Mockito.when(bookDaoMock.getById(1L)).thenReturn(Optional.of(bookToReturn));
        libraryService.borrowBookToReader(input);
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito.verify(bookDaoMock).update(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();
        capturedBook.getReader().ifPresentOrElse(
                reader -> assertEquals(2L, reader.getId(), "reader id is wrong"),
                Assertions::fail
        );
    }

    @ParameterizedTest(name = "case #{index}: invalid user input [{0}]")
    @DisplayName("Should fail borrow book to reader with invalid user input")
    @MethodSource("borrowBookToReader_not_ok_argumentsProvider")
    void borrowBookToReader_not_ok(String input, String expectedMessage) {
        var ex = assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input),
                "libraryService.borrowBookToReader(input) must throw ServiceLayerException when input is wrong.");
        assertEquals(expectedMessage, ex.getMessage(), "actual message in exception object is wrong");
    }
    static Stream<Arguments> borrowBookToReader_not_ok_argumentsProvider() {
        return Stream.of(
                arguments(null, "BorrowBookToReader input can't be null or empty."),
                arguments("", "BorrowBookToReader input can't be null or empty."),
                arguments("2.3", "BorrowBookToReader input must contain one '/' character."),
                arguments("2//3", "BorrowBookToReader input must contain one '/' character."),
                arguments("nrd/3", "The string does not contain a parsable long. For input string: \"nrd\""),
                arguments("2/bhb", "The string does not contain a parsable long. For input string: \"bhb\"")
        );
    }

    @Test
    @DisplayName("Should fail borrow book to reader with invalid user input")
    void borrowBookToReader_not_ok2() {
        String input7 = "1/99999";
        String expectedMessage7 = "Reader with ID 99999 does not exist in DB.";
        Mockito.when(bookDaoMock.getById(1L)).thenReturn(Optional.of(new Book()));
        var ex7 = assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input7),
                "libraryService.borrowBookToReader(input) must throw DaoLayerException when reader with passed id does not exist in DB");
        assertEquals(expectedMessage7, ex7.getMessage(), "actual message in exception object is wrong");

        String input8 = "99999/2";
        String expectedMessage8 = "Book with ID 99999 does not exist in DB.";
        Mockito.when(readerDaoMock.getById(2L)).thenReturn(Optional.of(new Reader()));
        var ex8 = assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input8),
                "libraryService.borrowBookToReader(input) must throw DaoLayerException when book with passed id does not exist in DB");
        assertEquals(expectedMessage8, ex8.getMessage(), "actual message in exception object is wrong");

        String input9 = "1/2";
        Reader readerToReturn = new Reader();
        Book bookToReturn = new Book();
        bookToReturn.setReader(Optional.of(readerToReturn));
        String expectedMessage9 = "Book with ID 1 has already borrowed by reader null. null.";
        Mockito.when(readerDaoMock.getById(2L)).thenReturn(Optional.of(readerToReturn));
        Mockito.when(bookDaoMock.getById(1L)).thenReturn(Optional.of(bookToReturn));
        var ex9 = assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input9),
                "libraryService.borrowBookToReader(input) must throw ServiceLayerException when book is already borrowed");
        assertEquals(expectedMessage9, ex9.getMessage(), "actual message in exception object is wrong");
    }

    @Test
    @DisplayName("Should successfully return book to library")
    void returnBookToLibrary_ok() {
        String input = "3";
        Book book = new Book();
        book.setId(3L);
        book.setReader(Optional.of(new Reader()));
        Mockito.when(bookDaoMock.getById(3L)).thenReturn(Optional.of(book));
        libraryService.returnBookToLibrary(input);
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito.verify(bookDaoMock).update(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();
        assertTrue(capturedBook.getReader().isEmpty(),
                "After returning field reader in book must be empty optional.");
    }

    @ParameterizedTest(name = "case #{index}: invalid user input [{0}]")
    @DisplayName("Should fail return book to library with invalid user input")
    @MethodSource("returnBookToLibrary_not_ok_argumentsProvider")
    void returnBookToLibrary_not_ok(String input, String expectedMessage) {
        var ex = assertThrows(ServiceLayerException.class,
                () -> libraryService.returnBookToLibrary(input),
                "libraryService.returnBookToLibrary(input) must throw ServiceLayerException when input is invalid");
        assertEquals(expectedMessage, ex.getMessage(), "actual message in exception object is wrong");
    }
    static Stream<Arguments> returnBookToLibrary_not_ok_argumentsProvider() {
        return Stream.of(
                arguments(null, "ReturnBookToLibrary input can't be null or empty."),
                arguments("", "ReturnBookToLibrary input can't be null or empty."),
                arguments("/", "The string / does not contain a parsable long. For input string: \"/\"")
        );
    }

    @Test
    @DisplayName("Should fail return book to library when book does not exist in DB.")
    void returnBookToLibrary_not_ok2() {
        String input4 = "99999";
        String expectedMessage4 = "Book with ID 99999 does not exist in DB.";
        Mockito.when(bookDaoMock.getById(99999L)).thenReturn(Optional.empty());
        var ex4 = assertThrows(ServiceLayerException.class,
                () -> libraryService.returnBookToLibrary(input4),
                "libraryService.returnBookToLibrary(input) must throw ServiceLayerException when book with passed id does not exist in DB");
        assertEquals(expectedMessage4, ex4.getMessage(), "actual message in exception object is wrong");
    }

    @Test
    @DisplayName("Should successfully get all books of reader")
    void getAllBooksOfReader_ok() {
        String input = "2";
        Mockito.when(readerDaoMock.getById(2L)).thenReturn(Optional.of(new Reader()));
        libraryService.getAllBooksOfReader(input);
        Mockito.verify(bookDaoMock, Mockito.times(1)).getBooksByReaderId(2L);
    }

    @ParameterizedTest(name = "case #{index}: invalid user input [{0}]")
    @DisplayName("Should fail get all books of reader with invalid user input")
    @MethodSource("getAllBooksOfReader_not_ok_argumentsProvider")
    void getAllBooksOfReader_not_ok(String input, String expectedMessage) {
        var ex = assertThrows(ServiceLayerException.class,
                () -> libraryService.getAllBooksOfReader(input),
                "libraryService.getAllBooksOfReader(input) must throw ServiceLayerException when user input is invalid");
        assertEquals(expectedMessage, ex.getMessage(), "actual message in exception object is wrong");
    }
    static Stream<Arguments> getAllBooksOfReader_not_ok_argumentsProvider() {
        return Stream.of(
                arguments(null, "getAllBooksOfReader input can't be null or empty."),
                arguments("", "getAllBooksOfReader input can't be null or empty."),
                arguments("j", "The string j does not contain a parsable long. For input string: \"j\"")
        );
    }

    @ParameterizedTest(name = "case #{index}: invalid user input [{0}]")
    @DisplayName("Should fail get reader of book with ID with invalid user input")
    @MethodSource("getReaderOfBookWithId_not_ok_argumentsProvider")
    void getReaderOfBookWithId_not_ok(String input, String expectedMessage) {
        var ex = assertThrows(ServiceLayerException.class,
                () -> libraryService.getReaderOfBookWithId(input),
                "libraryService.getReaderOfBookWithId(input) must throw ServiceLayerException when input is invalid");
        assertEquals(expectedMessage, ex.getMessage(), "actual message in exception object is wrong");
    }
    static Stream<Arguments> getReaderOfBookWithId_not_ok_argumentsProvider() {
        return Stream.of(
                arguments(null, "getReaderOfBookWithId input can't be null or empty."),
                arguments("", "getReaderOfBookWithId input can't be null or empty."),
                arguments("&", "The string & does not contain a parsable long. For input string: \"&\"")
        );
    }

    @Test
    @DisplayName("Should fail get reader of book with ID with invalid user input")
    void getReaderOfBookWithId_not_ok2() {
        String input4 = "99999";
        String expectedMessage4 = "Book with ID 99999 does not exist in DB.";
        Mockito.when(bookDaoMock.getById(99999L)).thenReturn(Optional.empty());
        var ex4 = assertThrows(ServiceLayerException.class,
                () -> libraryService.getReaderOfBookWithId(input4),
                "libraryService.getReaderOfBookWithId(input) must throw ServiceLayerException when book with passed id does not exist in DB");
        assertEquals(expectedMessage4, ex4.getMessage(), "actual message in exception object is wrong");
    }
}
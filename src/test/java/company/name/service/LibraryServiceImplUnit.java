package company.name.service;

import company.name.dao.BookDao;
import company.name.dao.ReaderDao;
import company.name.entities.Book;
import company.name.entities.Reader;
import company.name.exceptions.DaoLayerException;
import company.name.exceptions.ServiceLayerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceImplUnit {
    private final BookDao bookDaoMock = Mockito.mock(BookDao.class);
    private final ReaderDao readerDaoMock = Mockito.mock(ReaderDao.class);
    private final LibraryService libraryService = new LibraryServiceImpl(bookDaoMock, readerDaoMock);

    @Test
    void registerNewReader_ok() {
        String input = "Rudolf Mann";

        Reader readerToPass = new Reader();
        readerToPass.setName("Rudolf Mann");

        Reader readerToReturn = new Reader();
        readerToReturn.setId(444L);
        readerToReturn.setName("Rudolf Mann");

        Mockito.when(readerDaoMock.add(readerToPass)).thenReturn(readerToReturn);
        Reader createdReader = libraryService.registerNewReader(input);
        assertNotNull(createdReader, "libraryService.registerNewReader(input) returned null");
        assertNotNull(createdReader.getId(), "libraryService.registerNewReader(input) returned reader with id == null");
        assertEquals(444L, createdReader.getId(), "libraryService.registerNewReader(input) returned reader with wrong id");
        assertEquals("Rudolf Mann", createdReader.getName(), "libraryService.registerNewReader(input) returned reader with wrong name");
    }

    @Test
    void registerNewReader_not_ok() {
        String input1 = null;
        String expectedMessage1 = "Reader name can't be null or empty.";
        var ex1 = assertThrows(ServiceLayerException.class,
                () -> libraryService.registerNewReader(input1),
                "libraryService.registerNewReader(input) must throw ServiceLayerException when input == null");
        assertEquals(expectedMessage1, ex1.getMessage(), "actual message in exception object is wrong");

        String input2 = "";
        String expectedMessage2 = "Reader name can't be null or empty.";
        var ex2 = assertThrows(ServiceLayerException.class,
                () -> libraryService.registerNewReader(input2),
                "libraryService.registerNewReader(input) must throw ServiceLayerException when input.length() == 0");
        assertEquals(expectedMessage2, ex2.getMessage(), "actual message in exception object is wrong");


        String input3 = "Rudolf/Mann";
        String expectedMessage3 = "Reader name can't contain symbol '/'.";
        var ex3 = assertThrows(ServiceLayerException.class,
                () -> libraryService.registerNewReader(input3),
                "libraryService.registerNewReader(input) must throw ServiceLayerException when input contain /");
        assertEquals(expectedMessage3, ex3.getMessage(), "actual message in exception object is wrong");
    }

    @Test
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
        assertNotNull(createdBook, "libraryService.addNewBook(input) returned null");
        assertEquals(555L, createdBook.getId(), "libraryService.addNewBook(input) returned book with wrong id");
        assertEquals("Frank M. Carrano, Timothy M. Henry", createdBook.getAuthor(), "libraryService.addNewBook(input) returned book with wrong author");
        assertEquals("Data Structures and Abstractions with Java", createdBook.getTitle(), "libraryService.addNewBook(input) returned book with wrong title");
    }

    @Test
    void addNewBook_not_ok() {
        /*
        Book bookToReturn = new Book();
        bookToReturn.setId(555L);
        bookToReturn.setAuthor("Frank M. Carrano, Timothy M. Henry");
        bookToReturn.setTitle("Data Structures and Abstractions with Java");
        Mockito.when(bookDaoMock.add(ArgumentMatchers.any())).thenReturn(bookToReturn);
         */

        String input1 = null;
        assertThrows(ServiceLayerException.class,
                () -> libraryService.addNewBook(input1),
                "libraryService.addNewBook(input) must throw ServiceLayerException when input == null");

        String input2 = "";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.addNewBook(input2),
                "libraryService.addNewBook(input) must throw ServiceLayerException when input.length() == 0");

        String input3 = "/";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.addNewBook(input3),
                "libraryService.addNewBook(input) must throw ServiceLayerException when input = /");

        String input4 = "Data Structures and Abstractions with Java//Frank M. Carrano, Timothy M. Henry";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.addNewBook(input4),
                "libraryService.addNewBook(input) must throw ServiceLayerException when input contain not one /");

        String input5 = "/Frank M. Carrano, Timothy M. Henry";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.addNewBook(input5),
                "libraryService.addNewBook(input) must throw ServiceLayerException when input is invalid");

        String input6 = "Data Structures and Abstractions with Java/";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.addNewBook(input6),
                "libraryService.addNewBook(input) must throw ServiceLayerException when input is invalid");

        String input7 = " /Frank M. Carrano, Timothy M. Henry";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.addNewBook(input7),
                "libraryService.addNewBook(input) must throw ServiceLayerException when input is invalid");

        String input8 = "Data Structures and Abstractions with Java/ ";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.addNewBook(input8),
                "libraryService.addNewBook(input) must throw ServiceLayerException when input is invalid");
    }

    @Test
    void borrowBookToReader_ok() {
        String input = "1/2";
        Reader readerToReturn = new Reader();
        readerToReturn.setId(2L);
        Book bookToReturn = new Book();
        bookToReturn.setId(1L);
        bookToReturn.setReader(Optional.empty());
        Mockito.when(readerDaoMock.getById(2L)).thenReturn(Optional.of(readerToReturn));
        Mockito.when(bookDaoMock.getById(1L)).thenReturn(Optional.of(bookToReturn));
        Mockito.when(readerDaoMock.getReaderByBookId(1L)).thenReturn(Optional.of(readerToReturn));
        libraryService.borrowBookToReader(input);
        libraryService.getReaderOfBookWithId("1").ifPresentOrElse(
                reader -> assertEquals(2L, reader.getId(), "reader id does not equal to 2"),
                Assertions::fail
        );
    }

    @Test
    void borrowBookToReader_not_ok() {
        String input1 = null;
        assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input1),
                "libraryService.borrowBookToReader(input) must throw ServiceLayerException when input == null");

        String input2 = "";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input2),
                "libraryService.borrowBookToReader(input) must throw ServiceLayerException when input.length() == 0");

        String input3 = "2.3";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input3),
                "libraryService.borrowBookToReader(input) must throw ServiceLayerException when input does not contain /");

        String input4 = "2//3";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input4),
                "libraryService.borrowBookToReader(input) must throw ServiceLayerException when input contain not one /");

        String input5 = "nrd/3";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input5),
                "libraryService.borrowBookToReader(input) must throw ServiceLayerException when input does not contain parsable long");

        String input6 = "2/bhb";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input6),
                "libraryService.borrowBookToReader(input) must throw ServiceLayerException when input does not contain parsable long");

        String input7 = "1/99999";
        String expectedMessage7 = "Reader with ID 99999 does not exist in DB.";
        var ex7 = assertThrows(DaoLayerException.class,
                () -> libraryService.borrowBookToReader(input7),
                "libraryService.borrowBookToReader(input) must throw DaoLayerException when reader with passed id does not exist in DB");
        assertEquals(expectedMessage7, ex7.getMessage(), "actual message in exception object is wrong");

        String input8 = "99999/2";
        String expectedMessage8 = "Book with ID 99999 does not exist in DB.";
        Mockito.when(readerDaoMock.getById(2L)).thenReturn(Optional.of(new Reader()));
        var ex8 = assertThrows(DaoLayerException.class,
                () -> libraryService.borrowBookToReader(input8),
                "libraryService.borrowBookToReader(input) must throw DaoLayerException when book with passed id does not exist in DB");
        assertEquals(expectedMessage8, ex8.getMessage(), "actual message in exception object is wrong");

        String input9 = "1/2";
        Reader readerToReturn = new Reader();
        readerToReturn.setId(2L);
        Book bookToReturn = new Book();
        bookToReturn.setId(1L);
        Reader readerWhoReadsThisBook = new Reader();
        readerWhoReadsThisBook.setId(5L);
        readerWhoReadsThisBook.setName("Michael Muller");
        bookToReturn.setReader(Optional.of(readerWhoReadsThisBook));
        String expectedMessage9 = "Book with ID 1 has already borrowed by reader 5. Michael Muller";
        Mockito.when(readerDaoMock.getById(2L)).thenReturn(Optional.of(readerToReturn));
        Mockito.when(bookDaoMock.getById(1L)).thenReturn(Optional.of(bookToReturn));
        var ex9 = assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(input9),
                "libraryService.borrowBookToReader(input) must throw ServiceLayerException when book is already borrowed");
        assertEquals(expectedMessage9, ex9.getMessage(), "actual message in exception object is wrong");
    }

    @Test
    void returnBookToLibrary_ok() {
        String input = "3";
        Book book = new Book();
        book.setId(3L);
        Mockito.when(bookDaoMock.getById(3L)).thenReturn(Optional.of(book));
        libraryService.returnBookToLibrary(input);
        assertTrue(libraryService.getReaderOfBookWithId(input).isEmpty(),
                "After returning book must not be borrowed by any reader");
    }

    @Test
    void returnBookToLibrary_not_ok() {
        String input1 = null;
        String expectedMessage1 = "ReturnBookToLibrary input can't be null or empty.";
        var ex1 = assertThrows(ServiceLayerException.class,
                () -> libraryService.returnBookToLibrary(input1),
                "libraryService.returnBookToLibrary(input) must throw ServiceLayerException when input == null");
        assertEquals(expectedMessage1, ex1.getMessage(), "actual message in exception object is wrong");

        String input2 = "";
        String expectedMessage2 = "ReturnBookToLibrary input can't be null or empty.";
        var ex2 = assertThrows(ServiceLayerException.class,
                () -> libraryService.returnBookToLibrary(input2),
                "libraryService.returnBookToLibrary(input) must throw ServiceLayerException when input.length() == 0");
        assertEquals(expectedMessage2, ex2.getMessage(), "actual message in exception object is wrong");

        String input3 = "/";
        String expectedMessage3 = "The string / does not contain a parsable long. For input string: \"/\"";
        var ex3 = assertThrows(ServiceLayerException.class,
                () -> libraryService.returnBookToLibrary(input3),
                "libraryService.returnBookToLibrary(input) must throw ServiceLayerException when input does not contain a parsable Long");
        assertEquals(expectedMessage3, ex3.getMessage(), "actual message in exception object is wrong");

        String input4 = "99999";
        String expectedMessage4 = "Book with ID 99999 does not exist in DB.";
        Mockito.when(bookDaoMock.getById(99999L)).thenReturn(Optional.empty());
        var ex4 = assertThrows(ServiceLayerException.class,
                () -> libraryService.returnBookToLibrary(input4),
                "libraryService.returnBookToLibrary(input) must throw ServiceLayerException when book with passed id does not exist in DB");
        assertEquals(expectedMessage4, ex4.getMessage(), "actual message in exception object is wrong");
    }

    @Test
    void getAllBooksOfReader_ok() {
        String input1 = "2";
        Reader readerToReturn = new Reader();
        List<Book> listToReturn = new ArrayList<>();

        Book book1 = new Book();
        book1.setId(1L);
        book1.setAuthor("Herbert Schildt");
        book1.setTitle("Java. The Complete Reference. Twelfth Edition");
        listToReturn.add(book1);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setAuthor("Walter Savitch");
        book2.setTitle("Java. An Introduction to Problem Solving & Programming");
        listToReturn.add(book2);

        Mockito.when(readerDaoMock.getById(2L)).thenReturn(Optional.of(readerToReturn));
        Mockito.when(bookDaoMock.getBooksByReaderId(2L)).thenReturn(listToReturn);
        List<Book> allBooksOfReader = libraryService.getAllBooksOfReader(input1);
        assertNotNull(allBooksOfReader,
                "libraryService.getAllBooksOfReader(input) returned null");
        assertFalse(allBooksOfReader.isEmpty(),
                "libraryService.getAllBooksOfReader(input) returned empty list");
        assertTrue(allBooksOfReader.contains(book1),
                "returned list of books does not contain book1");
    }

    @Test
    void getAllBooksOfReader_not_ok() {
        String input1 = null;
        String expectedMessage1 = "getAllBooksOfReader input can't be null or empty.";
        var ex1 = assertThrows(ServiceLayerException.class,
                () -> libraryService.getAllBooksOfReader(input1),
                "libraryService.getAllBooksOfReader(input) must throw ServiceLayerException when input == null");
        assertEquals(expectedMessage1, ex1.getMessage(), "actual message in exception object is wrong");

        String input2 = "";
        String expectedMessage2 = "getAllBooksOfReader input can't be null or empty.";
        var ex2 = assertThrows(ServiceLayerException.class,
                () -> libraryService.getAllBooksOfReader(input2),
                "libraryService.getAllBooksOfReader(input) must throw ServiceLayerException when input.length() == 0");
        assertEquals(expectedMessage2, ex2.getMessage(), "actual message in exception object is wrong");

        String input3 = "j";
        String expectedMessage3 = "The string j does not contain a parsable long. For input string: \"j\"";
        var ex3 = assertThrows(ServiceLayerException.class,
                () -> libraryService.getAllBooksOfReader(input3),
                "libraryService.getAllBooksOfReader(input) must throw ServiceLayerException when input string does not contain a parsable long");
        assertEquals(expectedMessage3, ex3.getMessage(), "actual message in exception object is wrong");
    }

    @Test
    void getReaderOfBookWithId_not_ok() {
        String input1 = null;
        String expectedMessage1 = "getReaderOfBookWithId input can't be null or empty.";
        var ex1 = assertThrows(ServiceLayerException.class,
                () -> libraryService.getReaderOfBookWithId(input1),
                "libraryService.getReaderOfBookWithId(input) must throw ServiceLayerException when input == null");
        assertEquals(expectedMessage1, ex1.getMessage(), "actual message in exception object is wrong");

        String input2 = "";
        String expectedMessage2 = "getReaderOfBookWithId input can't be null or empty.";
        var ex2 = assertThrows(ServiceLayerException.class,
                () -> libraryService.getReaderOfBookWithId(input2),
                "libraryService.getReaderOfBookWithId(input) must throw ServiceLayerException when input.length() == 0");
        assertEquals(expectedMessage2, ex2.getMessage(), "actual message in exception object is wrong");

        String input3 = "&";
        String expectedMessage3 = "The string & does not contain a parsable long. For input string: \"&\"";
        var ex3 = assertThrows(ServiceLayerException.class,
                () -> libraryService.getReaderOfBookWithId(input3),
                "libraryService.getReaderOfBookWithId(input) must throw ServiceLayerException when input string does not contain a parsable long");
        assertEquals(expectedMessage3, ex3.getMessage(), "actual message in exception object is wrong");

        String input4 = "99999";
        String expectedMessage4 = "Book with ID 99999 does not exist in DB.";
        var ex4 = assertThrows(ServiceLayerException.class,
                () -> libraryService.getReaderOfBookWithId(input4),
                "libraryService.getReaderOfBookWithId(input) must throw ServiceLayerException when book with passed id does not exist in DB");
        assertEquals(expectedMessage4, ex4.getMessage(), "actual message in exception object is wrong");
    }

}
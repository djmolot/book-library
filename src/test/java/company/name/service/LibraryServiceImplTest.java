package company.name.service;

import company.name.entities.Book;
import company.name.entities.Reader;
import company.name.exceptions.ServiceLayerException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceImplTest {
    private static LibraryService libraryService;

    @BeforeAll
    static void beforeAll() {
        libraryService = new LibraryServiceImpl();
    }

    @Test
    void getAllBooks_ok() {
        List<Book> allBooks = libraryService.getAllBooks();
        assertNotNull(allBooks, "libraryService.getAllBooks() returned null");
        assertFalse(allBooks.isEmpty(), "libraryService.getAllBooks() returned empty list");
    }

    @Test
    void getAllBooks_not_ok() {
        //List<Book> allBooks = libraryService.getAllBooks();
    }

    @Test
    void getAllReaders_ok() {
        List<Reader> allReaders = libraryService.getAllReaders();
        assertNotNull(allReaders, "libraryService.getAllReaders() returned null");
        assertFalse(allReaders.isEmpty(), "libraryService.getAllReaders() returned empty list");
    }

    @Test
    void getAllReaders_not_ok() {
        //List<Reader> allReaders = libraryService.getAllReaders();
    }

    @Test
    void registerNewReader_ok() {
        String input = "Rudolf Mann";
        Reader newReader = libraryService.registerNewReader(input);
        assertNotNull(newReader, "libraryService.registerNewReader(input) returned null");
        assertNotNull(newReader.getId(), "");
        assertTrue(libraryService.getAllReaders().contains(newReader),
                "new Reader was not added to DB");
    }

    @Test
    void registerNewReader_not_ok() {
        String input1 = null;
        assertThrows(ServiceLayerException.class,
                () -> libraryService.registerNewReader(input1));

        String input2 = "";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.registerNewReader(input2));

        String input3 = "Rudolf/Mann";
        assertThrows(ServiceLayerException.class,
                () -> libraryService.registerNewReader(input3));
    }

    @Test
    void addNewBook_ok() {
        String input = "Data Structures and Abstractions with Java/Frank M. Carrano, Timothy M. Henry";
        Book newBook = libraryService.addNewBook(input);
        assertNotNull(newBook, "libraryService.addNewBook(input) returned null");
        assertTrue(libraryService.getAllBooks().contains(newBook),
                "new Book was not added to DB");
    }

    @Test
    void addNewBook_not_ok() {
        String input = null;
        libraryService.addNewBook(input);
    }

    @Test
    void borrowBookToReader_ok() {
        String input = null;
        libraryService.borrowBookToReader(input);
    }

    @Test
    void borrowBookToReader_not_ok() {
        String input = null;
        libraryService.borrowBookToReader(input);
    }
}
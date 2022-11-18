package company.name.service;

import company.name.dao.BookDao;
import company.name.dao.BookDaoPostgreSqlImpl;
import company.name.dao.ReaderDao;
import company.name.dao.ReaderDaoPostgreSqlImpl;
import company.name.dao.TestUtilDao;
import company.name.dao.TestUtilDaoImpl;
import company.name.entities.Book;
import company.name.entities.Reader;
import company.name.exceptions.ServiceLayerException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryServiceImplIT {
    private static LibraryService libraryService;
    private static TestUtilDao testUtilDao;

    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll()");
        BookDao bookDao = new BookDaoPostgreSqlImpl();
        ReaderDao readerDao = new ReaderDaoPostgreSqlImpl();
        libraryService = new LibraryServiceImpl(bookDao, readerDao);
        testUtilDao = new TestUtilDaoImpl();
    }

    @AfterAll
    static void afterAll() {
        System.out.println("afterAll()");
        System.out.println(" deleted all " + testUtilDao.deleteAllReaders() + " readers");
        System.out.println(" deleted all " + testUtilDao.deleteAllBooks() + " books");
    }

    @Test
    void getAllBooks_ok() {
        System.out.println("getAllBooks_ok()");
        List<Book> allBooks = libraryService.getAllBooks();
        assertNotNull(allBooks, "libraryService.getAllBooks() returned null");
        assertFalse(allBooks.isEmpty(), "libraryService.getAllBooks() returned empty list");
        //assertEquals(4, allBooks.size(), "the size of returned list of books is wrong");
        Book bookFromList = allBooks.get(1);
        assertEquals(2L, bookFromList.getId(), "id of book from list is wrong");
        assertEquals("Walter Savitch", bookFromList.getAuthor(), "autor of book from list is wrong");
        assertEquals("Java. An Introduction to Problem Solving & Programming", bookFromList.getTitle(),
                "title of book from list is wrong");
    }

    @Test
    void getAllReaders_ok() {
        System.out.println("getAllReaders_ok()");
        List<Reader> allReaders = libraryService.getAllReaders();
        assertNotNull(allReaders, "libraryService.getAllReaders() returned null");
        assertFalse(allReaders.isEmpty(), "libraryService.getAllReaders() returned empty list");
        //assertEquals(3, allReaders.size(), "the size of returned list of readers is wrong");
        Reader readerFromList = allReaders.get(1);
        assertEquals(2L, readerFromList.getId(), "id of reader from list is wrong");
        assertEquals("Voski Daniel", readerFromList.getName(), "name of reader from list is wrong");
    }

    @Test
    void registerNewReader_ok() {
        System.out.println("registerNewReader_ok()");
        String input = "Rudolf Mann";
        Reader newReader = libraryService.registerNewReader(input);
        assertNotNull(newReader, "libraryService.registerNewReader(input) returned null");
        assertNotNull(newReader.getId(), "libraryService.registerNewReader(input) returned reader with id == null");
        assertEquals(4L, newReader.getId(), "id of newly created reader is wrong");
        assertTrue(libraryService.getAllReaders().contains(newReader),
                "new Reader was not added to DB");
    }

    @Test
    void addNewBook_ok() {
        System.out.println("addNewBook_ok()");
        String input = "Data Structures and Abstractions with Java/Frank M. Carrano, Timothy M. Henry";
        Book createdBook = libraryService.addNewBook(input);
        assertNotNull(createdBook, "libraryService.addNewBook(input) returned null");
        assertNotNull(createdBook.getId(), "libraryService.addNewBook(input) returned Book with id == null");
        assertEquals(4L, createdBook.getId(), "id of newly created book is wrong");
        assertTrue(libraryService.getAllBooks().contains(createdBook), "new Book was not added to DB");
    }

    @Test
    void borrowBookToReader_ok() {
        System.out.println("borrowBookToReader_ok()");
        String input = "1/2";
        libraryService.borrowBookToReader(input);
        libraryService.getReaderOfBookWithId("1").ifPresentOrElse(
                reader -> assertEquals(2L, reader.getId(), "reader id does not equal to 2"),
                Assertions::fail
        );
    }

}
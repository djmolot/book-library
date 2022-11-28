package company.name.dao;

import company.name.entities.Book;
import company.name.entities.Reader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class BookDaoPostgreSqlImplIT {
    private static BookDao bookDao;
    private static ReaderDao readerDao;
    private static TestUtilDao testUtilDao;

    @BeforeAll
    static void beforeAll() {
        System.out.println("@BeforeAll");
        bookDao = new BookDaoPostgreSqlImpl();
        readerDao = new ReaderDaoPostgreSqlImpl();
        testUtilDao = new TestUtilDao();
        System.out.println(" deleted all " + testUtilDao.deleteAllReaders() + " readers");
        System.out.println(" deleted all " + testUtilDao.deleteAllBooks() + " books");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("@AfterAll");
        System.out.println(" deleted all " + testUtilDao.deleteAllReaders() + " readers");
        System.out.println(" deleted all " + testUtilDao.deleteAllBooks() + " books");
    }

    @BeforeEach
    void setUp() {
        System.out.println("@BeforeEach");
        System.out.println(" deleted all " + testUtilDao.deleteAllReaders() + " readers");
        System.out.println(" deleted all " + testUtilDao.deleteAllBooks() + " books");
    }

    @AfterEach
    void tearDown() {
        System.out.println("@AfterEach");
        System.out.println(" deleted all " + testUtilDao.deleteAllReaders() + " readers");
        System.out.println(" deleted all " + testUtilDao.deleteAllBooks() + " books");
    }

    @Test
    @DisplayName("Should successfully add new book to DB")
    void add_ok() {
        System.out.println(" add_ok()");
        String title = "book title";
        String author = "book author";
        Book bookToSave = new Book();
        bookToSave.setTitle(title);
        bookToSave.setAuthor(author);
        Book savedBook = bookDao.add(bookToSave);
        assertAll(
                () -> assertNotNull(savedBook, "bookDao.add(bookToSave) must not return null."),
                () -> assertTrue(savedBook.getId() != null && savedBook.getId() > 0, "added to DB book must have ID > 0."),
                () -> assertEquals(author, savedBook.getAuthor(), "author of added to DB book is wrong."),
                () -> assertEquals(title, savedBook.getTitle(), "title of added to DB book is wrong.")
        );
    }

    @Test
    @DisplayName("Should successfully get book by ID")
    void getById_ok() {
        String author = "Walter Savitch";
        String title = "Java. An Introduction to Problem Solving & Programming.";
        Book addedBook = addNewBookToDB(author, title);
        Optional<Book> bookByIdOptional = bookDao.getById(addedBook.getId());
        Book bookById = bookByIdOptional.orElse(null);
        assertNotNull(bookById, "bookDao.getById(id) can't return empty optional for existing in DB book.");
        assertAll(
                () -> assertEquals(1L, bookById.getId(), "ID of returned book is wrong."),
                () -> assertEquals(author, bookById.getAuthor(), "author of returned book is wrong."),
                () -> assertEquals(title, bookById.getTitle(), "title of returned book is wrong.")
        );
    }

    @Test
    @DisplayName("Should successfully get all books from DB")
    void getAll_ok() {
        String author1 = "Herbert Schildt";
        String title1 = "Java. The Complete Reference. Twelfth Edition.";
        addNewBookToDB(author1, title1);
        String author2 = "Walter Savitch";
        String title2 = "Java. An Introduction to Problem Solving & Programming.";
        addNewBookToDB(author2, title2);
        List<Book> allBooks = bookDao.getAll();
        assertAll(
                () -> assertFalse(allBooks.isEmpty()),
                () -> assertEquals(2, allBooks.size())
        );
        Book book1 = allBooks.get(0);
        assertEquals(1L, book1.getId(), "ID of returned book is wrong.");
        assertEquals(author1, book1.getAuthor(), "author of returned book is wrong.");
        assertEquals(title1, book1.getTitle(), "title of returned book is wrong.");
        Book book2 = allBooks.get(1);
        assertEquals(2L, book2.getId(), "ID of returned book is wrong.");
        assertEquals(author2, book2.getAuthor(), "author of returned book is wrong.");
        assertEquals(title2, book2.getTitle(), "title of returned book is wrong.");
    }

    @Test
    @DisplayName("Should successfully update book in DB")
    void update_ok() {
        String author1 = "Herbert Schildt";
        String title1 = "Java. The Complete Reference. Twelfth Edition.";
        Book addedBook = addNewBookToDB(author1, title1);
        Reader reader1 = new Reader();
        reader1.setName("Zhirayr Hovik");
        Reader addedReader = readerDao.add(reader1);
        addedBook.setReader(Optional.of(addedReader));
        Book updatedBook = bookDao.update(addedBook);
        Reader readerFromBook = updatedBook.getReader().orElse(null);
        assertNotNull(readerFromBook, "reader from updated book must not be empty optional.");
        assertAll(
                () -> assertEquals(1L, readerFromBook.getId(), "reader from updated book has wrong ID."),
                () -> assertEquals("Zhirayr Hovik", readerFromBook.getName(), "reader from updated book has wrong name.")
        );
    }

    @Test
    @DisplayName("Should successfully get all books of reader by reader ID")
    void getBooksByReaderId() {
        String author1 = "Herbert Schildt";
        String title1 = "Java. The Complete Reference. Twelfth Edition.";
        Book book1 = addNewBookToDB(author1, title1);
        String author2 = "Walter Savitch";
        String title2 = "Java. An Introduction to Problem Solving & Programming.";
        Book book2 = addNewBookToDB(author2, title2);
        Reader reader = new Reader();
        reader.setName("Zhirayr Hovik");
        Reader addedReader = readerDao.add(reader);
        book1.setReader(Optional.of(addedReader));
        bookDao.update(book1);
        book2.setReader(Optional.of(addedReader));
        bookDao.update(book2);
        List<Book> booksOfReader = bookDao.getBooksByReaderId(addedReader.getId());
        assertAll(
                () -> assertFalse(booksOfReader.isEmpty(),
                        "bookDao.getBooksByReaderId(id) must not return empty list for existing in DB reader which has borrowed books."),
                () -> assertEquals(2, booksOfReader.size(), "size of returned list of reader books is wrong.")
        );
        Book book1OfReader = booksOfReader.get(0);
        assertEquals(1L, book1OfReader.getId(), "book from list of reader books has wrong ID.");
        assertEquals(author1, book1OfReader.getAuthor(), "book from list of reader books has wrong author.");
        assertEquals(title1, book1OfReader.getTitle(), "book from list of reader books has wrong title.");
        Book book2OfReader = booksOfReader.get(1);
        assertEquals(2L, book2OfReader.getId(), "book from list of reader books has wrong ID.");
        assertEquals(author2, book2OfReader.getAuthor(), "book from list of reader books has wrong author.");
        assertEquals(title2, book2OfReader.getTitle(), "book from list of reader books has wrong title.");
    }

    private Book addNewBookToDB(String author, String title) {
        Book bookToAdd = new Book();
        bookToAdd.setAuthor(author);
        bookToAdd.setTitle(title);
        return bookDao.add(bookToAdd);
    }
}
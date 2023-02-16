package company.name.library.repositories;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class BookRepositoryJdbcTemplImplIT {
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final List<Book> expectedBooks;
    private final List<Reader> expectedReaders;

    @Autowired
    BookRepositoryJdbcTemplImplIT(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.expectedReaders = expectedReaders();
        this.expectedBooks = expectedBooks();
    }

    @Test
    void add_should_return_added_book() {
        Book expectedBook = newBook();
        Book actualBook = bookRepository.add(expectedBook);
        Assertions.assertNotNull(actualBook.getId(),"ID of added Book should not be Null.");
        Assertions.assertEquals(expectedBook, actualBook, "ActualBook should be equal to ExpectedBook");
    }

    @Test
    void getById_should_return_existing_book() {
        Book expectedBook2 = expectedBooks().get(1);
        Book actualBook2 = bookRepository.getById(2L).orElse(null);
        Assertions.assertNotNull(actualBook2,"Method should not return empty Optional.");
        Assertions.assertEquals(expectedBook2, actualBook2, "ActualBook should be equal to ExpectedBook");
    }

    @Test
    void getById_should_return_empty_Optional_if_Book_with_this_Id_does_not_exist_in_DB() {
        Optional<Book> bookOpt = bookRepository.getById(555L);
        Assertions.assertTrue(bookOpt.isEmpty(),
                "getById() should return empty Optional if Book with this Id does not exist in DB.");
    }

    @Test
    void deleteById_should_delete_existing_book() {
        boolean deleteById = bookRepository.deleteById(2L);
        Assertions.assertTrue(deleteById,"Method should return TRUE.");
        var books = bookRepository.getAll();
        Assertions.assertEquals(2, books.size(),
                "books.size should be equal to 2 after deleting a Book.");
    }

    @Test
    void deleteById_should_return_false_if_Book_with_this_Id_does_not_exist_in_DB() {
        boolean deleteById = bookRepository.deleteById(555L);
        Assertions.assertFalse(deleteById,
                "deleteById() should return false if Book with this Id does not exist in DB. ");
    }

    @Test
    public void getAll_should_return_list_equal_to_expected() {
        List<Book> actualBooks = bookRepository.getAll();
        Assertions.assertEquals(expectedBooks, actualBooks, "actualBooks should be equal to expectedBooks");
    }

    @Test
    void update_should_change_reader_id_value_new() {
        Book expectedBook3 = expectedBooks.get(2);
        Reader expectedReader1 = expectedReaders.get(0) ;
        expectedBook3.setReader(Optional.of(expectedReader1));
        bookRepository.update(expectedBook3);
        Book actualBook3 = bookRepository.getById(3L).orElse(null);
        Assertions.assertNotNull(actualBook3,"actualBook3 should not be NULL.");
        Assertions.assertEquals(expectedBook3, actualBook3,
                "actualBook3 should be equal to expectedBook3");

        expectedBook3.setReader(Optional.empty());
        bookRepository.update(expectedBook3);
        actualBook3 = bookRepository.getById(3L).orElse(null);
        Assertions.assertNotNull(actualBook3);
        Assertions.assertEquals(expectedBook3, actualBook3,
                "actualBook3 should be equal to expectedBook3");
    }

    @Test
    void update_should_change_reader_id_value() {
        Book book2 = bookRepository.getById(2L).orElse(null);
        Assertions.assertNotNull(book2,
                "Book with ID=2L should exist in DB.");
        Reader reader3 = readerRepository.getById(3L).orElse(null);
        Assertions.assertNotNull(reader3,
                "Reader with ID=3L should exist in DB.");
        book2.setReader(Optional.of(reader3));
        bookRepository.update(book2);
        var readerId = bookRepository.getById(book2.getId())
                .flatMap(Book::getReader)
                .map(Reader::getId)
                .orElse(null);
        Assertions.assertNotNull(readerId,
                "Reader of updated Book should not be NULL.");
        Assertions.assertEquals(3L, readerId,
                "reader_id of updated Book should be equal to 3L.");

        book2.setReader(Optional.empty());
        bookRepository.update(book2);
        readerId = bookRepository.getById(book2.getId())
                .flatMap(Book::getReader)
                .map(Reader::getId)
                .orElse(null);
        Assertions.assertNull(readerId,
                "reader_id of updated Book should be NULL.");
    }

    @Test
    void getBooksByReaderId_should_return_list_of_Books() {
        List<Book> expectedBooksOfReader2 = expectedReaders.get(1).getBooks();
        List<Book> actualBooksOfReader2 = bookRepository.getBooksByReaderId(2L);
        Assertions.assertEquals(expectedBooksOfReader2, actualBooksOfReader2,
                "Reader with ID=2L should have 3 borrowed Books.");
    }

    @Test
    void getBooksByReaderId_should_return_empty_list() {
        List<Book> booksOfReader1 = bookRepository.getBooksByReaderId(1L);
        Assertions.assertEquals(0, booksOfReader1.size(),
                "Reader with ID=1L should not have borrowed Books.");
    }

    @Test
    void getBooksByReaderId_should_return_empty_list_if_Reader_with_this_ID_does_not_exist_in_DB() {
        List<Book> booksOfReader = bookRepository.getBooksByReaderId(555L);
        Assertions.assertTrue(booksOfReader.isEmpty());
    }

    private Book book1() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Java. The Complete Reference. Twelfth Edition");
        book1.setAuthor("Herbert Schildt");
        return book1;
    }

    private Book book2() {
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Java. An Introduction to Problem Solving & Programming");
        book2.setAuthor("Walter Savitch");
        return book2;
    }

    private Book book3() {
        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Data Structures And Algorithms Made Easy In JAVA");
        book3.setAuthor("Narasimha Karumanchi");
        return book3;
    }

    private Reader reader1() {
        Reader reader1 = new Reader();
        reader1.setId(1L);
        reader1.setName("Zhirayr Hovik");
        return reader1;
    }

    private Reader reader2() {
        Reader reader2 = new Reader();
        reader2.setId(2L);
        reader2.setName("Voski Daniel");
        return reader2;
    }

    private Reader reader3() {
        Reader reader3 = new Reader();
        reader3.setId(3L);
        reader3.setName("Ruben Nazaret");
        return reader3;
    }

    private List<Book> expectedBooks() {
        Book book1 = book1();
        book1.setReader(Optional.of(reader2()));
        Book book2 = book2();
        book2.setReader(Optional.of(reader2()));
        Book book3 = book3();
        book3.setReader(Optional.empty());
        return List.of(book1, book2, book3);
    }

    private List<Reader> expectedReaders() {
        Reader reader1 = reader1();
        Reader reader2 = reader2();
        reader2.setBooks(List.of(book1(), book2()));
        Reader reader3 = reader3();
        return List.of(reader1, reader2, reader3);
    }

    private Book newBook() {
        Book newBook = new Book();
        newBook.setAuthor("Rebecca Serle");
        newBook.setTitle("One Italian Summer");
        newBook.setReader(Optional.empty());
        return newBook;
    }
}

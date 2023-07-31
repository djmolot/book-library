package company.name.library.repositories;

import company.name.library.TestDataProducer;
import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class BookRepositoryImplIT {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @BeforeEach
    void setUp() {
        readerRepository.deleteAllAndRestartIdSequence();
        bookRepository.deleteAllAndRestartIdSequence();

        Book book1 = TestDataProducer.newBook1();
        bookRepository.add(book1);
        Book book2 = TestDataProducer.newBook2();
        bookRepository.add(book2);
        Book book3 = TestDataProducer.newBook3();
        bookRepository.add(book3);

        Reader reader1 = TestDataProducer.newReader1();
        readerRepository.add(reader1);
        Reader reader2 = TestDataProducer.newReader2();
        readerRepository.add(reader2);
        Reader reader3 = TestDataProducer.newReader3();
        readerRepository.add(reader3);

        book1.setReader(Optional.of(reader2));
        bookRepository.updateBorrowDetails(book1);
        book2.setReader(Optional.of(reader2));
        bookRepository.updateBorrowDetails(book2);
    }

    @Test
    void addShouldReturnAddedBookWithReaderEmptyOptional() {
        var newBook = TestDataProducer.newBook();
        var expectedTitle = newBook.getTitle();
        var expectedAuthor = newBook.getAuthor();
        Assertions.assertNull(newBook.getId(),
                "Id of original book must be null");
        Assertions.assertTrue(newBook.getReader().isEmpty(),
                "Optional of reader in original book must be empty");

        var savedBook = bookRepository.add(newBook);
        Assertions.assertNotNull(savedBook.getId(),
                "Id of saved book must not be null");
        Assertions.assertEquals(expectedTitle, savedBook.getTitle(),
                "Title of saved book must be equal to expected");
        Assertions.assertEquals(expectedAuthor, savedBook.getAuthor(),
                "Author of saved book must be equal to expected");
        Assertions.assertTrue(savedBook.getReader().isEmpty(),
                "Optional of reader in saved book must be empty");
    }

    @Test
    void getByIdShouldReturnExistingBook() {
        Map<Long, Book> allBooksMap = TestDataProducer.newAllBooksMap();
        Long bookId = 2L;
        Book expectedBook2 = allBooksMap.get(bookId);
        Book actualBook2 = bookRepository.getById(bookId).orElse(null);
        Assertions.assertNotNull(actualBook2,"Method should not return empty Optional.");
        Assertions.assertEquals(expectedBook2, actualBook2,
                "ActualBook should be equal to ExpectedBook");
    }

    @Test
    void getByIdShouldReturnEmptyOptionalIfBookWithThisIdDoesNotExistInDb() {
        Long bookId = 555L;
        Optional<Book> bookOptional = bookRepository.getById(bookId);
        Assertions.assertTrue(bookOptional.isEmpty(),
                "getById() should return empty Optional if Book with this Id does not exist in DB.");
    }

    @Test
    public void getAllShouldReturnListEqualToExpected() {
        List<Book> expectedBooks = TestDataProducer.newAllBooksList();
        List<Book> actualBooks = bookRepository.getAll();
        Assertions.assertEquals(expectedBooks, actualBooks,
                "actualBooks should be equal to expectedBooks");
    }

    @Test
    void updateShouldChangeReaderIdValue() {
        Map<Long, Book> allBooksMap = TestDataProducer.newAllBooksMap();
        Map<Long, Reader> allReadersMap = TestDataProducer.newAllReadersMap();
        Long bookId = 3L;
        Long readerId = 1L;
        Book originalBook3 = allBooksMap.get(bookId);
        Reader originalReader1 = allReadersMap.get(readerId);
        originalBook3.setReader(Optional.of(originalReader1));
        var expectedId = originalBook3.getId();
        var expectedTitle = originalBook3.getTitle();
        var expectedAuthor = originalBook3.getAuthor();
        var expectedReader = originalBook3.getReader();
        Book updatedBook3 = bookRepository.updateBorrowDetails(originalBook3);

        Assertions.assertNotNull(updatedBook3,"updatedBook3 should not be null");
        Assertions.assertEquals(expectedId, updatedBook3.getId(),
                "Id of updated book must be equal to expected");
        Assertions.assertEquals(expectedTitle, updatedBook3.getTitle(),
                "Title of updated book must be equal to expected");
        Assertions.assertEquals(expectedAuthor, updatedBook3.getAuthor(),
                "Author of updated book must be equal to expected");
        Assertions.assertEquals(expectedReader, updatedBook3.getReader(),
                "Reader of updated book must be equal to expected");

        originalBook3.setReader(Optional.empty());
        updatedBook3 = bookRepository.updateBorrowDetails(originalBook3);
        Assertions.assertNotNull(updatedBook3,"updatedBook3 should not be null");
        Assertions.assertEquals(expectedId, updatedBook3.getId(),
                "Id of updated book must be equal to expected");
        Assertions.assertEquals(expectedTitle, updatedBook3.getTitle(),
                "Title of updated book must be equal to expected");
        Assertions.assertEquals(expectedAuthor, updatedBook3.getAuthor(),
                "Author of updated book must be equal to expected");
        Assertions.assertTrue(updatedBook3.getReader().isEmpty(),
                "ReaderOptional in updated book must be empty");
    }

    @Test
    void getBooksByReaderIdShouldReturnListEqualToExpected() {
        Map<Long, Reader> allReadersMap = TestDataProducer.newAllReadersMap();
        Long readerId = 2L;
        List<Book> expectedBooksOfReader2 = allReadersMap.get(readerId).getBooks();
        Assertions.assertNotNull(expectedBooksOfReader2);
        Assertions.assertEquals(2, expectedBooksOfReader2.size());
        List<Book> actualBooksOfReader2 = bookRepository.getBooksByReaderId(readerId);
        Assertions.assertEquals(expectedBooksOfReader2, actualBooksOfReader2,
                "actualBooksOfReader2 should be equal to expectedBooksOfReader2");
    }

    @Test
    void getBooksByReaderIdShouldReturnEmptyListIfReaderHasNotBorrowedAnyBook() {
        Long readerId = 1L;
        List<Book> booksOfReader1 = bookRepository.getBooksByReaderId(readerId);
        Assertions.assertTrue(booksOfReader1.isEmpty(),
                "Reader with ID=1L should not have borrowed Books.");
    }

}

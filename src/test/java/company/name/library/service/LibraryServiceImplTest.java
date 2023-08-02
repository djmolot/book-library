package company.name.library.service;

import company.name.library.TestDataProducer;
import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.ServiceLayerException;
import company.name.library.repositories.BookRepository;
import company.name.library.repositories.ReaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;

@Slf4j
@SpringBootTest
class LibraryServiceImplTest {
    @InjectMocks
    private LibraryServiceImpl libraryService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ReaderRepository readerRepository;
    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    @Value("${library.maxNumberOfBooksToBorrow}")
    private int maxNumberOfBooksToBorrow;
    @Value("${library.minAgeOfReaderForRestricted}")
    private int minAgeOfReaderForRestricted;


    @BeforeEach
    void setUp() {
        Field maxNumberOfBooksField = ReflectionUtils
                .findField(LibraryServiceImpl.class, "maxNumberOfBooksToBorrow");
        maxNumberOfBooksField.setAccessible(true);
        ReflectionUtils.setField(maxNumberOfBooksField, libraryService, maxNumberOfBooksToBorrow);

        Field minAgeOfReaderField = ReflectionUtils
                .findField(LibraryServiceImpl.class, "minAgeOfReaderForRestricted");
        minAgeOfReaderField.setAccessible(true);
        ReflectionUtils.setField(minAgeOfReaderField, libraryService, minAgeOfReaderForRestricted);
    }

    @Test
    void borrowBookToReaderShouldAddReaderToBook() {
        Long bookId = 3L;
        Long readerId = 1L;
        Book book3 = TestDataProducer.newBook3();
        Assertions.assertTrue(book3.getReader().isEmpty(), "Reader of book3 must be empty optional");
        Reader reader1 = TestDataProducer.newReader1();
        Assertions.assertNull(reader1.getBooks(),"reader1.books must be null");

        Mockito.when(bookRepository.getById(bookId)).thenReturn(Optional.of(book3));
        Mockito.when(readerRepository.getById(readerId)).thenReturn(Optional.of(reader1));
        Mockito.when(bookRepository.getBooksByReaderId(readerId)).thenReturn(List.of());

        libraryService.borrowBookToReader(bookId, readerId);

        verify(bookRepository).getById(bookId);
        verify(readerRepository).getById(readerId);
        verify(bookRepository).getBooksByReaderId(readerId);

        verify(bookRepository).updateBorrowDetails(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();
        Assertions.assertNotNull(capturedBook,
                "capturedBook should not be null");
        Assertions.assertEquals(bookId, capturedBook.getId(),
                "capturedBook id should be equal to bookId");
        Assertions.assertEquals(book3.getTitle(), capturedBook.getTitle(),
                "capturedBook title should be equal to expected title");
        Assertions.assertEquals(book3.getAuthor(), capturedBook.getAuthor(),
                "capturedBook author should be equal to expected author");
        Assertions.assertTrue(capturedBook.getBorrowDate().isPresent(),
                "capturedBook borrowDate should be set");

        Reader capturedReader = capturedBook.getReader().orElse(null);
        Assertions.assertNotNull(capturedReader, "capturedReader should not be null");
        Assertions.assertEquals(readerId, capturedReader.getId(),
                "id of capturedReader should be equal to expected");
        Assertions.assertEquals(reader1.getName(), capturedReader.getName(),
                "name of capturedReader should be equal to expected");
        Assertions.assertEquals(reader1.getBirthDate(), capturedReader.getBirthDate(),
                "birthDate of capturedReader should be equal to expected");
        Assertions.assertNull(capturedReader.getBooks(),
                "books of capturedReader should be null");
    }

    @ParameterizedTest(name = "case #{index}: {0}")
    @MethodSource("borrowBookToReaderNotOkArgumentsProvider")
    @DisplayName("borrowBookToReaderShouldThrowExceptionWhen")
    @SuppressWarnings("unused")
    void borrowBookToReaderNotOk(String caseName, Long bookId, Long readerId, String expMessage) {
        Map<Long, Book> allBooksMap = TestDataProducer.newAllBooksMap();
        Map<Long, Reader> allReadersMap = TestDataProducer.newAllReadersMap();
        Book book = allBooksMap.get(bookId);
        Reader reader = allReadersMap.get(readerId);
        Optional<Book> bookOptional = book != null ? Optional.of(book) : Optional.empty();
        Optional<Reader> readerOptional = reader != null ? Optional.of(reader) : Optional.empty();
        Mockito.when(bookRepository.getById(bookId)).thenReturn(bookOptional);
        Mockito.when(readerRepository.getById(readerId)).thenReturn(readerOptional);
        Exception exception = Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(bookId, readerId),
                "borrowBookToReader should throw ServiceLayerException");
        Assertions.assertEquals(expMessage, exception.getMessage(),"Exception message is wrong");
    }
    static Stream<Arguments> borrowBookToReaderNotOkArgumentsProvider() {
        return Stream.of(
                arguments("readerDoesNotExistInDb", 3L, 555L,
                        "Reader with ID 555 does not exist in DB."),
                arguments("bookDoesNotExistInDb", 777L, 1L,
                        "Book with ID 777 does not exist in DB."),
                arguments("bookIsAlreadyBorrowed", 2L, 1L,
                        "Book with ID 2 has already borrowed by reader Reader(id=2, name=Voski Daniel, books=null).")
        );
    }

    @Test
    void returnBookToLibraryShouldSetEmptyOptionalToFieldReaderOfBook() {
        Map<Long, Book> allBooksMap = TestDataProducer.newAllBooksMap();
        Long bookId = 2L;
        Book book2 = allBooksMap.get(bookId);
        Assertions.assertTrue(book2.getReader().isPresent(),
                "field reader of book2 should not be empty Optional");
        Mockito.when(bookRepository.getById(bookId)).thenReturn(Optional.of(book2));
        Book book2WithEmptyReader = TestDataProducer.newBook2();
        book2WithEmptyReader.setReader(Optional.empty());
        Mockito.when(bookRepository.updateBorrowDetails(book2WithEmptyReader)).thenReturn(book2WithEmptyReader);
        Book actualBook = libraryService.returnBookToLibrary(bookId);
        Assertions.assertEquals(bookId, actualBook.getId(),
                "actualBook id should be equal to bookId");
        Assertions.assertEquals(book2.getTitle(), actualBook.getTitle(),
                "actualBook title should be equal to expectedTitle");
        Assertions.assertEquals(book2.getAuthor(), actualBook.getAuthor(),
                "actualBook author should be equal to expectedAuthor");
        Assertions.assertTrue(actualBook.getReader().isEmpty(),
                "actualBook reader should be empty optional");
    }

    @ParameterizedTest(name = "case #{index}: {0}")
    @MethodSource("returnBookToLibraryNotOkArgumentsProvider")
    @DisplayName("returnBookToLibraryShouldThrowExceptionWhen")
    void returnBookToLibraryNotOk(String caseName, Long bookId, String expMessage) {
        Map<Long, Book> allBooksMap = TestDataProducer.newAllBooksMap();
        Book book = allBooksMap.get(bookId);
        Optional<Book> bookOptional = book != null ? Optional.of(book) : Optional.empty();
        Mockito.when(bookRepository.getById(bookId)).thenReturn(bookOptional);
        Exception exception = Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.returnBookToLibrary(bookId),
                "returnBookToLibrary should throw ServiceLayerException");
        Assertions.assertEquals(expMessage, exception.getMessage(),"Exception message is wrong");
    }
    static Stream<Arguments> returnBookToLibraryNotOkArgumentsProvider() {
        return Stream.of(
                arguments("bookDoesNotExistInDb", 777L,
                        "Book with ID 777 does not exist in DB."),
                arguments("bookIsNotBorrowed", 3L,
                        "Book with ID 3 is not borrowed by any reader.")
        );
    }

    @Test
    void getAllBooksOfReaderShouldReturnListWhichWasReturnedByBookRepository() {
        Map<Long, Reader> allReadersMap = TestDataProducer.newAllReadersMap();
        Long readerId = 2L;
        Reader reader2 = allReadersMap.get(readerId);
        Assertions.assertTrue(reader2.getBooks() != null && reader2.getBooks().size() == 2,
                "books of reader2 should not be null and should have size equal to 2");
        List<Book> expectedBooksOfReader2 = reader2.getBooks();
        Mockito.when(readerRepository.getById(readerId)).thenReturn(Optional.of(reader2));
        Mockito.when(bookRepository.getBooksByReaderId(readerId)).thenReturn(expectedBooksOfReader2);
        List<Book> actualBooksOfReader2 = libraryService.getAllBooksOfReader(readerId);
        Assertions.assertEquals(expectedBooksOfReader2, actualBooksOfReader2,
                "actualBooksOfReader2 should be equal to expectedBooksOfReader2");
        Assertions.assertSame(expectedBooksOfReader2, actualBooksOfReader2,
                "expectedBooksOfReader2 and actualBooksOfReader2 should refer to the same object");
    }

    @Test
    void getAllBooksOfReaderShouldThrowExceptionWhenReaderDoesNotExistInDb() {
        Long readerId = 555L;
        String expMessage = "Reader with ID 555 does not exist in DB.";
        Mockito.when(readerRepository.getById(readerId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.getAllBooksOfReader(readerId),
                "getAllBooksOfReader should throw exception when reader does not exist in DB");
        Assertions.assertEquals(expMessage, exception.getMessage(),"Exception message is wrong");

    }

    @Test
    void getReaderOfBookWithIdShouldReturnReaderWichWasReturnedByReaderRepository() {
        Map<Long, Book> allBooksMap = TestDataProducer.newAllBooksMap();
        Long bookId = 2L;
        Book book2 = allBooksMap.get(bookId);
        Assertions.assertTrue(book2.getReader().isPresent(),
                "Reader in book2 should be present");
        Reader expectedReader = book2.getReader().get();
        Mockito.when(bookRepository.getById(bookId)).thenReturn(Optional.of(book2));
        Mockito.when(readerRepository.getReaderByBookId(bookId)).thenReturn(Optional.of(expectedReader));
        Optional<Reader> actualReaderOpt = libraryService.getReaderOfBookWithId(bookId);
        Reader actualReader = actualReaderOpt.orElseGet(
                () -> Assertions.fail("Actual reader of book2 must be not empty optional"));
        Assertions.assertEquals(expectedReader, actualReader,
                "actualReader should be equal to expectedReader");
        Assertions.assertSame(expectedReader, actualReader,
                "expectedReader and actualReader should refer to the same object");
    }

    @Test
    void getReaderOfBookWithIdShouldThrowExceptionWhenBookDoesNotExistInDb() {
        Long bookId = 777L;
        String expMessage = "Book with ID 777 does not exist in DB.";
        Mockito.when(bookRepository.getById(bookId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.getReaderOfBookWithId(bookId),
                "getReaderOfBookWithId should throw exception when book does not exist in DB");
        Assertions.assertEquals(expMessage, exception.getMessage(),"Exception message is wrong");

    }

}
package company.name.library.service;

import company.name.library.TestDataProducer;
import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.ServiceLayerException;
import company.name.library.repositories.BookRepository;
import company.name.library.repositories.ReaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
@ExtendWith(SpringExtension.class)
class LibraryServiceImplTest {
    @InjectMocks
    private LibraryServiceImpl libraryService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ReaderRepository readerRepository;

    @Test
    void borrowBookToReader_should_add_reader_to_book() {
        Long bookId = 3L;
        Long readerId = 1L;
        Book book3 = TestDataProducer.newBook3();
        book3.setReader(Optional.empty());
        Reader reader1 = TestDataProducer.newReader1();
        Mockito.when(bookRepository.getById(bookId)).thenReturn(Optional.of(book3));
        Mockito.when(readerRepository.getById(readerId)).thenReturn(Optional.of(reader1));
        Book bookForUpdate = TestDataProducer.newBook3();
        bookForUpdate.setReader(Optional.of(TestDataProducer.newReader1()));
        Mockito.when(bookRepository.update(bookForUpdate)).thenReturn(bookForUpdate);

        Book actualBook = libraryService.borrowBookToReader(bookId, readerId);
        Assertions.assertNotNull(actualBook, "Actual book should not be null");
        Assertions.assertEquals(bookId, actualBook.getId(),
                "actualBook id should be equal to bookId");
        Assertions.assertEquals(book3.getTitle(), actualBook.getTitle(),
                "actualBook title should be equal to expectedTitle");
        Assertions.assertEquals(book3.getAuthor(), actualBook.getAuthor(),
                "actualBook author should be equal to expectedAuthor");

        Reader actualReader = actualBook.getReader().orElse(null);
        Assertions.assertNotNull(actualReader, "actualReader should not be null");
        Assertions.assertEquals(reader1.getId(), actualReader.getId(),
                "Id of actual reader should be equal to reader1.getId()");
        Assertions.assertEquals(reader1.getName(), actualReader.getName(),
                "Name of actual reader should be equal to reader1.getName()");
    }

    @ParameterizedTest(name = "case #{index}: [{0}]")
    @MethodSource("borrowBookToReader_not_ok_argumentsProvider")
    @DisplayName("borrowBookToReader_should_throw_exception_when")
    void borrowBookToReader_not_ok(String methodName, Long bookId, Long readerId, String expMessage) {
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
    static Stream<Arguments> borrowBookToReader_not_ok_argumentsProvider() {
        return Stream.of(
                arguments("reader_does_not_exist_in_DB", 3L, 555L,
                        "Reader with ID 555 does not exist in DB."),
                arguments("book_does_not_exist_in_DB", 777L, 1L,
                        "Book with ID 777 does not exist in DB."),
                arguments("book_is_already_borrowed", 2L, 1L,
                        "Book with ID 2 has already borrowed by reader Reader(id=2, name=Voski Daniel, books=null).")
        );
    }

    @Test
    void returnBookToLibrary_should_set_empty_optional_to_field_reader_of_book() {
        Map<Long, Book> allBooksMap = TestDataProducer.newAllBooksMap();
        Long bookId = 2L;
        Book book2 = allBooksMap.get(bookId);
        Assertions.assertTrue(book2.getReader().isPresent(),
                "field reader of book2 should not be empty Optional");
        Mockito.when(bookRepository.getById(bookId)).thenReturn(Optional.of(book2));
        Book book2WithEmptyReader = TestDataProducer.newBook2();
        book2WithEmptyReader.setReader(Optional.empty());
        Mockito.when(bookRepository.update(book2WithEmptyReader)).thenReturn(book2WithEmptyReader);
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

    @ParameterizedTest(name = "case #{index}: [{0}]")
    @MethodSource("returnBookToLibrary_not_ok_argumentsProvider")
    @DisplayName("returnBookToLibrary_should_throw_exception_when")
    void returnBookToLibrary_not_ok(String methodName, Long bookId, String expMessage) {
        Map<Long, Book> allBooksMap = TestDataProducer.newAllBooksMap();
        Book book = allBooksMap.get(bookId);
        Optional<Book> bookOptional = book != null ? Optional.of(book) : Optional.empty();
        Mockito.when(bookRepository.getById(bookId)).thenReturn(bookOptional);
        Exception exception = Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.returnBookToLibrary(bookId),
                "returnBookToLibrary should throw ServiceLayerException");
        Assertions.assertEquals(expMessage, exception.getMessage(),"Exception message is wrong");
    }
    static Stream<Arguments> returnBookToLibrary_not_ok_argumentsProvider() {
        return Stream.of(
                arguments("book_does_not_exist_in_DB", 777L,
                        "Book with ID 777 does not exist in DB."),
                arguments("book_is_not_borrowed", 3L,
                        "Book with ID 3 does not borrowed by any reader.")
        );
    }

    @Test
    void getAllBooksOfReader_should_return_list_which_was_returned_by_bookRepository() {
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
    void getAllBooksOfReader_should_throw_exception_when_reader_does_not_exist_in_DB() {
        Long readerId = 555L;
        String expMessage = "Reader with ID 555 does not exist in DB.";
        Mockito.when(readerRepository.getById(readerId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.getAllBooksOfReader(readerId),
                "getAllBooksOfReader should throw exception when reader does not exist in DB");
        Assertions.assertEquals(expMessage, exception.getMessage(),"Exception message is wrong");

    }

    @Test
    void getReaderOfBookWithId_should_return_reader_wich_was_returned_by_readerRepository() {
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
    void getReaderOfBookWithId_should_throw_exception_when_book_does_not_exist_in_DB() {
        Long bookId = 777L;
        String expMessage = "Book with ID 777 does not exist in DB.";
        Mockito.when(bookRepository.getById(bookId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.getReaderOfBookWithId(bookId),
                "getReaderOfBookWithId should throw exception when book does not exist in DB");
        Assertions.assertEquals(expMessage, exception.getMessage(),"Exception message is wrong");

    }

}
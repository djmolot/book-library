package company.name.library.service;

import company.name.library.TestDatabaseData;
import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.ServiceLayerException;
import company.name.library.repositories.BookRepository;
import company.name.library.repositories.ReaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
class LibraryServiceImplTest {
    @InjectMocks
    private LibraryServiceImpl libraryService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ReaderRepository readerRepository;

    private final TestDatabaseData testDatabaseData = new TestDatabaseData();
    private List<Book> expectedBooks;
    private List<Reader> expectedReaders;

    @BeforeEach
    public void setUp() {
        expectedBooks = testDatabaseData.getTestBooks();
        expectedReaders = testDatabaseData.getTestReaders();
    }

    @Test
    void borrowBookToReader_should_borrow_book_to_reader() {
        Book book3 = testDatabaseData.book3();
        book3.setReader(Optional.empty());
        Reader reader1 = testDatabaseData.reader1();
        Mockito.when(bookRepository.getById(3L)).thenReturn(Optional.of(book3));
        Mockito.when(readerRepository.getById(1L)).thenReturn(Optional.of(reader1));
        Book bookForUpdate = testDatabaseData.book3();
        bookForUpdate.setReader(Optional.of(testDatabaseData.reader1()));
        Mockito.when(bookRepository.update(bookForUpdate)).thenReturn(bookForUpdate);
        Book actualBook = libraryService.borrowBookToReader(3L, 1L);
        Assertions.assertEquals(bookForUpdate, actualBook,
                "actualBook should be equal to bookForUpdate");
    }

    @Test
    void borrowBookToReader_should_throw_exception_when_reader_does_not_exist_in_DB() {
        Mockito.when(bookRepository.getById(3L)).thenReturn(Optional.of(expectedBooks.get(2)));
        Mockito.when(readerRepository.getById(555L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(3L, 555L),
                "method should throw ServiceLayerException when reader does not exist in DB");
    }

    @Test
    void borrowBookToReader_should_throw_exception_when_book_does_not_exist_in_DB() {
        Mockito.when(bookRepository.getById(777L)).thenReturn(Optional.empty());
        Mockito.when(readerRepository.getById(1L)).thenReturn(Optional.of(expectedReaders.get(0)));
        Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(777L, 1L),
                "method should throw ServiceLayerException when book does not exist in DB");
    }

    @Test
    void borrowBookToReader_should_throw_exception_when_book_is_already_borrowed() {
        Mockito.when(bookRepository.getById(2L)).thenReturn(Optional.of(expectedBooks.get(1)));
        Mockito.when(readerRepository.getById(1L)).thenReturn(Optional.of(expectedReaders.get(0)));
        Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.borrowBookToReader(2L, 1L),
                "method should throw ServiceLayerException when book is already borrowed");
    }

    @Test
    void returnBookToLibrary_should_set_empty_optional_to_reader_field() {
        Mockito.when(bookRepository.getById(2L)).thenReturn(Optional.of(expectedBooks.get(1)));
        Book bookForUpdate = expectedBooks.get(1);
        Book expectedBook = testDatabaseData.book2();
        expectedBook.setReader(Optional.empty());
        Mockito.when(bookRepository.update(bookForUpdate)).thenReturn(expectedBook);
        Book actualBook = libraryService.returnBookToLibrary(2L);
        Assertions.assertEquals(expectedBook, actualBook,
                "actualBook should be equal to expectedBook");
    }

    @Test
    void returnBookToLibrary_should_throw_exception_when_book_does_not_exist_in_DB() {
        Mockito.when(bookRepository.getById(777L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.returnBookToLibrary(777L),
                "method should throw ServiceLayerException when book does not exist in DB");
    }

    @Test
    void returnBookToLibrary_should_throw_exception_when_book_is_not_borrowed() {
        Mockito.when(bookRepository.getById(3L)).thenReturn(Optional.of(expectedBooks.get(2)));
        Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.returnBookToLibrary(3L),
                "method should throw ServiceLayerException when book does not exist in DB");
    }

    @Test
    void getAllBooksOfReader_should_return_list_of_books() {
        Mockito.when(readerRepository.getById(2L)).thenReturn(Optional.of(expectedReaders.get(1)));
        List<Book> expectedBooksOfReader = expectedReaders.get(1).getBooks();
        Mockito.when(bookRepository.getBooksByReaderId(2L)).thenReturn(expectedBooksOfReader);
        List<Book> actualBooksOfReader = libraryService.getAllBooksOfReader(2L);
        Assertions.assertEquals(expectedBooksOfReader, actualBooksOfReader,
                "actualBooksOfReader should be equal to expectedBooksOfReader");
    }

    @Test
    void getAllBooksOfReader_should_throw_exception_when_reader_does_not_exist_in_DB() {
        Mockito.when(readerRepository.getById(555L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.getAllBooksOfReader(555L),
                "method should throw exception when reader does not exist in DB");
    }

    @Test
    void getReaderOfBookWithId_should_return_reader_of_book() {
        Mockito.when(bookRepository.getById(2L)).thenReturn(Optional.of(expectedBooks.get(1)));
        Optional<Reader> expectedReaderOpt = expectedBooks.get(1).getReader();
        Mockito.when(readerRepository.getReaderByBookId(2L)).thenReturn(expectedReaderOpt);
        Optional<Reader> actualReaderOpt = libraryService.getReaderOfBookWithId(2L);
        Assertions.assertEquals(expectedReaderOpt, actualReaderOpt,
                "actualReaderOpt should be equal to expectedReaderOpt");
    }

    @Test
    void getReaderOfBookWithId_should_throw_exception_when_book_does_not_exist_in_DB() {
        Mockito.when(bookRepository.getById(777L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceLayerException.class,
                () -> libraryService.getReaderOfBookWithId(777L),
                "metod should throw exception when book does not exist in DB");
    }

}
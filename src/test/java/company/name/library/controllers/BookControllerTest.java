package company.name.library.controllers;

import company.name.library.TestDataProducer;
import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.ServiceLayerException;
import company.name.library.service.LibraryService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    @MockBean
    private LibraryService libraryService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void showAllBooksShouldReturnListOfBooks() {
        List<Book> expectedBooks = TestDataProducer.newAllBooksList();
        int expectedBooksSize = expectedBooks.size();
        Assertions.assertEquals(3, expectedBooksSize,
                "expectedBooksSize should be equal to 3");
        Reader readerOfBook1 = expectedBooks.get(0).getReader().orElse(null);
        Assertions.assertNotNull(readerOfBook1,
                "Reader of book1 must be present");
        Reader readerOfBook2 = expectedBooks.get(1).getReader().orElse(null);
        Assertions.assertNotNull(readerOfBook2,
                "Reader of book2 must be present");
        Reader readerOfBook3 = expectedBooks.get(2).getReader().orElse(null);
        Assertions.assertNull(readerOfBook3,
                "Reader of book3 must be null");
        Mockito.when(libraryService.getAllBooks()).thenReturn(expectedBooks);
        RestAssuredMockMvc.when()
                .get("/api/v1/library/books")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(expectedBooksSize))
                .body("[0].id", Matchers.equalTo(expectedBooks.get(0).getId().intValue()))
                .body("[0].title", Matchers.equalTo(expectedBooks.get(0).getTitle()))
                .body("[0].author", Matchers.equalTo(expectedBooks.get(0).getAuthor()))
                .body("[0].reader", Matchers.notNullValue())
                .body("[0].reader.id", Matchers.equalTo(readerOfBook1.getId().intValue()))
                .body("[0].reader.name", Matchers.equalTo(readerOfBook1.getName()))
                .body("[1].id", Matchers.equalTo(expectedBooks.get(1).getId().intValue()))
                .body("[1].title", Matchers.equalTo(expectedBooks.get(1).getTitle()))
                .body("[1].author", Matchers.equalTo(expectedBooks.get(1).getAuthor()))
                .body("[1].reader", Matchers.notNullValue())
                .body("[1].reader.id", Matchers.equalTo(readerOfBook2.getId().intValue()))
                .body("[1].reader.name", Matchers.equalTo(readerOfBook2.getName()))
                .body("[2].id", Matchers.equalTo(expectedBooks.get(2).getId().intValue()))
                .body("[2].title", Matchers.equalTo(expectedBooks.get(2).getTitle()))
                .body("[2].author", Matchers.equalTo(expectedBooks.get(2).getAuthor()))
                .body("[2].reader", Matchers.nullValue());
    }

    @Test
    void showAllBooksShouldReturnEmptyListWhenTableBooksIsEmpty() {
        Mockito.when(libraryService.getAllBooks()).thenReturn(List.of());
        RestAssuredMockMvc.when()
                .get("/api/v1/library/books")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(0));
    }

    @Test
    void addNewBookShouldAddValidBook() {
        Book newBook = TestDataProducer.newBook();
        String expectedTitle = newBook.getTitle();
        String expectedAuthor = newBook.getAuthor();
        Book savedBook = TestDataProducer.newBook();
        Long bookId = 4L;
        savedBook.setId(bookId);
        Mockito.when(libraryService.addNewBook(newBook)).thenReturn(savedBook);
        RestAssuredMockMvc.given()
                .contentType(JSON)
                .body(newBook)
                .when()
                .post("/api/v1/library/books")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.equalTo(bookId.intValue()))
                .body("title", Matchers.equalTo(expectedTitle))
                .body("author", Matchers.equalTo(expectedAuthor))
                .body("reader", Matchers.nullValue());
    }

    @ParameterizedTest(name = "case #{index}: {0}")
    @MethodSource("addNewBookNotOkArgumentsProvider")
    @DisplayName("addNewBookShouldFailToAddNewBookWhen")
    void addNewBookNotOk(String caseName, Book book, String expectedMessage, String expectedDetails) {
        RestAssuredMockMvc.given()
                .contentType(JSON)
                .body(book)
                .when()
                .post("/api/v1/library/books")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errorMessage", Matchers.equalTo(expectedMessage))
                .body("errors[0].constraint", Matchers.equalTo(expectedDetails));
    }
    static Stream<Arguments> addNewBookNotOkArgumentsProvider() {
        return Stream.of(
                arguments("bookTitleIsInvalid", TestDataProducer.bookWithInvalidTitle(),
                        "ArgumentValidation Error", "Title must be between 10 and 255 characters"),
                arguments("bookAuthorIsInvalid", TestDataProducer.bookWithInvalidAuthor(),
                        "ArgumentValidation Error", "Author must be between 3 and 50 characters")
        );
    }

    @Test
    void borrowBookToReaderShouldBorrowFreeBookToValidReader() {
        Map<Long, Book> allBooksMap = TestDataProducer.newAllBooksMap();
        Map<Long, Reader> allReadersMap = TestDataProducer.newAllReadersMap();
        Long bookId = 3L;
        Long readerId = 1L;
        Book expectedBook = allBooksMap.get(bookId);
        Reader expectedReader = allReadersMap.get(readerId);
        expectedBook.setReader(Optional.of(expectedReader));
        Mockito.when(libraryService.borrowBookToReader(bookId, readerId))
                .thenReturn(expectedBook);
        RestAssuredMockMvc.post("/api/v1/library/books/{bookId}/readers/{readerId}", bookId, readerId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(bookId.intValue()))
                .body("title", Matchers.equalTo(expectedBook.getTitle()))
                .body("author", Matchers.equalTo(expectedBook.getAuthor()))
                .body("reader.id", Matchers.equalTo(readerId.intValue()))
                .body("reader.name", Matchers.equalTo(expectedReader.getName()));
    }

    @Test
    void returnBookToLibraryShouldSetFieldReaderToNull() {
        Map<Long, Book> allBooksMap = TestDataProducer.newAllBooksMap();
        Long bookId = 1L;
        Book book1 = allBooksMap.get(bookId);
        String expectedTitle = book1.getTitle();
        String expectedAuthor = book1.getAuthor();
        book1.setReader(Optional.empty());
        Mockito.when(libraryService.returnBookToLibrary(bookId)).thenReturn(book1);
        RestAssuredMockMvc.when()
                .delete("/api/v1/library/books/{bookId}/readers", bookId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(bookId.intValue()))
                .body("title", Matchers.equalTo(expectedTitle))
                .body("author", Matchers.equalTo(expectedAuthor))
                .body("reader", Matchers.equalTo(null));
    }

    @Test
    void showReaderOfBookShouldReturnReader() {
        Map<Long, Reader> allReadersMap = TestDataProducer.newAllReadersMap();
        Long bookId = 1L;
        Long readerId = 2L;
        Reader expectedReader = allReadersMap.get(readerId);
        Mockito.when(libraryService.getReaderOfBookWithId(bookId)).thenReturn(Optional.of(expectedReader));
        RestAssuredMockMvc.get("/api/v1/library/books/{bookId}/readers", bookId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(readerId.intValue()))
                .body("name", Matchers.equalTo(expectedReader.getName()));
    }

    @Test
    void showReaderOfBookShouldReturnEmptyOptional() {
        Long bookId = 3L;
        Mockito.when(libraryService.getReaderOfBookWithId(bookId)).thenReturn(Optional.empty());
        RestAssuredMockMvc.get("/api/v1/library/books/{bookId}/readers", bookId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(""));
    }

    @Test
    void showReaderOfBookShouldThrowExceptionWhenBookWithPassedIdDoesNotExistInDb() {
        Long bookId = 777L;
        Mockito.when(libraryService.getReaderOfBookWithId(bookId))
                .thenThrow(new ServiceLayerException("Book with ID " + bookId + " does not exist in DB."));
        RestAssuredMockMvc.get("/api/v1/library/books/{bookId}/readers", bookId)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errorMessage", Matchers.equalTo("Service layer Error"))
                .body("errors[0].constraint", Matchers.equalTo("Book with ID "
                        + bookId + " does not exist in DB."));
    }

}
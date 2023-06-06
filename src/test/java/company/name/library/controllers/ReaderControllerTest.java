package company.name.library.controllers;

import company.name.library.TestDataProducer;
import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.service.LibraryService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static io.restassured.http.ContentType.JSON;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class ReaderControllerTest {
    @MockBean
    private LibraryService libraryService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void showAllReadersShouldReturnListOfAllReadersWithTheirBooks() {
        List<Reader> expectedReaders = TestDataProducer.newAllReadersList();
        Assertions.assertEquals(3, expectedReaders.size());
        int expectedSize = expectedReaders.size();
        List<Book> expectedBooks1 = expectedReaders.get(0).getBooks();
        Assertions.assertEquals(0, expectedBooks1.size());
        List<Book> expectedBooks2 = expectedReaders.get(1).getBooks();
        Assertions.assertEquals(2, expectedBooks2.size());
        List<Book> expectedBooks3 = expectedReaders.get(2).getBooks();
        Assertions.assertEquals(0, expectedBooks3.size());
        Mockito.when(libraryService.getAllReaders()).thenReturn(expectedReaders);
        RestAssuredMockMvc.when()
                .get("/api/v1/readers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(expectedSize))
                .body("[0].id", Matchers.equalTo(expectedReaders.get(0).getId().intValue()))
                .body("[0].name", Matchers.equalTo(expectedReaders.get(0).getName()))
                .body("[0].books.size()", Matchers.equalTo(0))
                .body("[1].id", Matchers.equalTo(expectedReaders.get(1).getId().intValue()))
                .body("[1].name", Matchers.equalTo(expectedReaders.get(1).getName()))
                .body("[1].books.size()", Matchers.equalTo(2))
                .body("[1].books[0].id", Matchers.equalTo(expectedBooks2.get(0).getId().intValue()))
                .body("[1].books[0].title", Matchers.equalTo(expectedBooks2.get(0).getTitle()))
                .body("[1].books[0].author", Matchers.equalTo(expectedBooks2.get(0).getAuthor()))
                .body("[1].books[0].reader", Matchers.nullValue())
                .body("[1].books[1].id", Matchers.equalTo(expectedBooks2.get(1).getId().intValue()))
                .body("[1].books[1].title", Matchers.equalTo(expectedBooks2.get(1).getTitle()))
                .body("[1].books[1].author", Matchers.equalTo(expectedBooks2.get(1).getAuthor()))
                .body("[1].books[1].reader", Matchers.nullValue())
                .body("[2].id", Matchers.equalTo(expectedReaders.get(2).getId().intValue()))
                .body("[2].name", Matchers.equalTo(expectedReaders.get(2).getName()))
                .body("[2].books.size()", Matchers.equalTo(0));
    }

    @Test
    void showAllReadersShouldReturnEmptyListWhenTableReadersIsEmpty() {
        Mockito.when(libraryService.getAllReaders()).thenReturn(List.of());
        RestAssuredMockMvc.when()
                .get("/api/v1/readers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(0));
    }

    @Test
    void registerNewReaderShouldAddValidReaderWithEmptyBooksList() {
        Reader newReader = TestDataProducer.newReader();
        String expectedName = newReader.getName();
        Reader savedReader = TestDataProducer.newReader();
        Long readerId = 4L;
        savedReader.setId(readerId);
        Mockito.when(libraryService.registerNewReader(newReader)).thenReturn(savedReader);
        RestAssuredMockMvc.given()
                .contentType(JSON)
                .body(newReader)
                .when()
                .post("/api/v1/readers")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.equalTo(readerId.intValue()))
                .body("name", Matchers.equalTo(expectedName))
                .body("books.size()", Matchers.equalTo(0));
    }

    @Test
    void registerNewReaderShouldFailWithInvalidReaderName() {
        Reader newReader = TestDataProducer.readerWithInvalidName();
        String readerName = newReader.getName();
        Assertions.assertTrue(readerName.length() < 3,
                "readerName should contain less then 3 characters");
        RestAssuredMockMvc.given()
                .contentType(JSON)
                .body(newReader)
                .when()
                .post("/api/v1/readers")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errorMessage", Matchers.equalTo("ArgumentValidation Error"))
                .body("errors[0].constraint", Matchers.equalTo("Reader name must be between 3 and 50 characters"));
    }

    @Test
    void showAllBooksBorrowedByReaderShouldReturnListOfBooksWithReaderNull() {
        Map<Long, Reader> allReadersMap = TestDataProducer.newAllReadersMap();
        Long readerId = 2L;
        List<Book> booksOfReader2 = allReadersMap.get(readerId).getBooks();
        int expectedSize = booksOfReader2.size();
        Assertions.assertEquals(2, expectedSize,
                "size of list of reader2 books should be equal to 2");
        Mockito.when(libraryService.getAllBooksOfReader(readerId)).thenReturn(booksOfReader2);
        RestAssuredMockMvc.when()
                .get("/api/v1/readers/{readerId}/books", readerId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(expectedSize))
                .body("[0].id", Matchers.equalTo(booksOfReader2.get(0).getId().intValue()))
                .body("[0].title", Matchers.equalTo(booksOfReader2.get(0).getTitle()))
                .body("[0].author", Matchers.equalTo(booksOfReader2.get(0).getAuthor()))
                .body("[0].reader", Matchers.nullValue())
                .body("[1].id", Matchers.equalTo(booksOfReader2.get(1).getId().intValue()))
                .body("[1].title", Matchers.equalTo(booksOfReader2.get(1).getTitle()))
                .body("[1].author", Matchers.equalTo(booksOfReader2.get(1).getAuthor()))
                .body("[1].reader", Matchers.nullValue());
        }

}
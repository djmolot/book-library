package company.name.library.controllers;

import company.name.library.TestDatabaseData;
import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.service.LibraryService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
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

import static io.restassured.http.ContentType.JSON;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class ReaderControllerTest {
    @MockBean
    private LibraryService libraryService;
    @Autowired
    private MockMvc mockMvc;

    private final TestDatabaseData testDatabaseData = new TestDatabaseData();
    private List<Reader> expectedReaders;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        expectedReaders = testDatabaseData.getTestReaders();
    }

    @Test
    void showAllReaders_should_return_list_of_readers() {
        Mockito.when(libraryService.getAllReaders()).thenReturn(expectedReaders);
        RestAssuredMockMvc.when()
                .get("/api/v1/library/readers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(3))
                .body("[0].id", Matchers.equalTo(expectedReaders.get(0).getId().intValue()))
                .body("[0].name", Matchers.equalTo(expectedReaders.get(0).getName()))
                .body("[1].id", Matchers.equalTo(expectedReaders.get(1).getId().intValue()))
                .body("[1].name", Matchers.equalTo(expectedReaders.get(1).getName()))
                .body("[2].id", Matchers.equalTo(expectedReaders.get(2).getId().intValue()))
                .body("[2].name", Matchers.equalTo(expectedReaders.get(2).getName()));
    }

    @Test
    void showAllReaders_should_return_empty_list_when_table_readers_is_empty() {
        Mockito.when(libraryService.getAllReaders()).thenReturn(List.of());
        RestAssuredMockMvc.when()
                .get("/api/v1/library/readers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(0));
    }

    @Test
    void registerNewReader_should_add_valid_reader() {
        Reader readerToPass = testDatabaseData.newReader();
        Reader readerToReturn = testDatabaseData.newReader();
        readerToReturn.setId(4L);
        Mockito.when(libraryService.registerNewReader(readerToPass)).thenReturn(readerToReturn);
        RestAssuredMockMvc.given()
                .contentType(JSON)
                .body(readerToPass)
                .when()
                .post("/api/v1/library/readers")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.equalTo(readerToReturn.getId().intValue()))
                .body("name", Matchers.equalTo(readerToReturn.getName()));
    }

    @Test
    void registerNewReader_should_fail_with_invalid_reader_name() {
        Reader readerToPass = readerWithInvalidName();
        RestAssuredMockMvc.given()
                .contentType(JSON)
                .body(readerToPass)
                .when()
                .post("/api/v1/library/readers")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

        @Test
    void showAllBooksBorrowedByReader_should_return_list_of_books() {
        List<Book> booksOfReader2 = expectedReaders.get(1).getBooks();
        Mockito.when(libraryService.getAllBooksOfReader(2L)).thenReturn(booksOfReader2);
        RestAssuredMockMvc.when()
                .get("/api/v1/library/readers/2/books")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(2))
                .body("[0].id", Matchers.equalTo(booksOfReader2.get(0).getId().intValue()))
                .body("[0].title", Matchers.equalTo(booksOfReader2.get(0).getTitle()))
                .body("[0].author", Matchers.equalTo(booksOfReader2.get(0).getAuthor()))
                .body("[1].id", Matchers.equalTo(booksOfReader2.get(1).getId().intValue()))
                .body("[1].title", Matchers.equalTo(booksOfReader2.get(1).getTitle()))
                .body("[1].author", Matchers.equalTo(booksOfReader2.get(1).getAuthor()));
        }

    private Reader readerWithInvalidName() {
        Reader reader = new Reader();
        reader.setName("Ad");
        return reader;
    }

}
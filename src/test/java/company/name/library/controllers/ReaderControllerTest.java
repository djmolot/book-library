package company.name.library.controllers;

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
import java.util.Optional;

import static io.restassured.http.ContentType.JSON;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class ReaderControllerTest {
    @MockBean
    private LibraryService libraryService;
    @Autowired
    private MockMvc mockMvc;
    private final List<Reader> expectedReaders;

    ReaderControllerTest() {
        this.expectedReaders = expectedReaders();
    }

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void showAllReaders_should_return_list_of_readers() {
        Mockito.when(libraryService.getAllReaders()).thenReturn(expectedReaders);
        RestAssuredMockMvc.when()
                .get("/api/v1/library/readers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(3))
                .body("[0].id", Matchers.equalTo(reader1().getId().intValue()))
                .body("[0].name", Matchers.equalTo(reader1().getName()))
                .body("[1].id", Matchers.equalTo(reader2().getId().intValue()))
                .body("[1].name", Matchers.equalTo(reader2().getName()))
                .body("[2].id", Matchers.equalTo(reader3().getId().intValue()))
                .body("[2].name", Matchers.equalTo(reader3().getName()));
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
        Reader readerToPass = newReader();
        Reader readerToReturn = newReader();
        readerToReturn.setId(4L);
        Mockito.when(libraryService.registerNewReader(readerToPass)).thenReturn(readerToReturn);
        RestAssuredMockMvc.given()
                .contentType(JSON)
                .body(readerToPass)
                .when()
                .post("/api/v1/library/readers")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.equalTo(4))
                .body("name", Matchers.equalTo(readerToPass.getName()));
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
        List<Book> expectedBooks = expectedReaders.get(1).getBooks();
        Mockito.when(libraryService.getAllBooksOfReader(2L)).thenReturn(expectedBooks);
        RestAssuredMockMvc.when()
                .get("/api/v1/library/readers/2/books")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(2))
                .body("[0].id", Matchers.equalTo(book1().getId().intValue()))
                .body("[0].title", Matchers.equalTo(book1().getTitle()))
                .body("[0].author", Matchers.equalTo(book1().getAuthor()))
                .body("[1].id", Matchers.equalTo(book2().getId().intValue()))
                .body("[1].title", Matchers.equalTo(book2().getTitle()))
                .body("[1].author", Matchers.equalTo(book2().getAuthor()));
        }

    private Book book1() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Java. The Complete Reference. Twelfth Edition");
        book1.setAuthor("Herbert Schildt");
        book1.setReader(Optional.empty());
        return book1;
    }

    private Book book2() {
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Java. An Introduction to Problem Solving & Programming");
        book2.setAuthor("Walter Savitch");
        book2.setReader(Optional.empty());
        return book2;
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

    private List<Reader> expectedReaders() {
        Reader reader1 = reader1();
        Reader reader2 = reader2();
        reader2.setBooks(List.of(book1(), book2()));
        Reader reader3 = reader3();
        return List.of(reader1, reader2, reader3);
    }

    private Reader newReader() {
        Reader newReader = new Reader();
        newReader.setName("Addy Morra");
        return newReader;
    }

    private Reader readerWithInvalidName() {
        Reader reader = new Reader();
        reader.setName("Ad");
        return reader;
    }

}
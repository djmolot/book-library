package company.name.library.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.ServiceLayerException;
import company.name.library.service.LibraryService;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasItems;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    @MockBean
    private LibraryService libraryService;
    @Autowired
    private MockMvc mockMvc;
    private final List<Book> expectedBooks;
    private final List<Reader> expectedReaders;

    BookControllerTest() {
        this.expectedBooks = expectedBooks();
        this.expectedReaders = expectedReaders();
    }

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void showAllBooks_should_return_list_of_books() {
        Mockito.when(libraryService.getAllBooks()).thenReturn(expectedBooks);
        RestAssuredMockMvc.when()
                .get("/api/v1/library/books")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(3))
                .body("[0].id", Matchers.equalTo(book1().getId().intValue()))
                .body("[0].title", Matchers.equalTo(book1().getTitle()))
                .body("[0].author", Matchers.equalTo(book1().getAuthor()))
                .body("[1].id", Matchers.equalTo(book2().getId().intValue()))
                .body("[1].title", Matchers.equalTo(book2().getTitle()))
                .body("[1].author", Matchers.equalTo(book2().getAuthor()))
                .body("[2].id", Matchers.equalTo(book3().getId().intValue()))
                .body("[2].title", Matchers.equalTo(book3().getTitle()))
                .body("[2].author", Matchers.equalTo(book3().getAuthor()));
    }

    @Test
    void showAllBooks_should_return_empty_list_when_table_books_is_empty() {
        Mockito.when(libraryService.getAllBooks()).thenReturn(List.of());
        RestAssuredMockMvc.when()
                .get("/api/v1/library/books")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(0));
    }

    @Test
    void addNewBook_should_add_valid_book() {
        Book bookToPass = newBook();
        Book bookToReturn = newBook();
        bookToReturn.setId(4L);
        Mockito.when(libraryService.addNewBook(bookToPass)).thenReturn(bookToReturn);
        RestAssuredMockMvc.given()
                .contentType(JSON)
                .body(bookToPass)
                .when()
                .post("/api/v1/library/books")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.equalTo(4))
                .body("title", Matchers.equalTo("One Italian Summer"))
                .body("author", Matchers.equalTo("Rebecca Serle"));
    }

    @Test
    void addNewBook_should_fail_with_invalid_book_title() {
        Book bookToPass = bookWithInvalidTitle();
        RestAssuredMockMvc.given()
                .contentType(JSON)
                .body(bookToPass)
                .when()
                .post("/api/v1/library/books")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void addNewBook_should_fail_with_invalid_book_author() {
        Book bookToPass = bookWithInvalidAuthor();
        RestAssuredMockMvc.given()
                .contentType(JSON)
                .body(bookToPass)
                .when()
                .post("/api/v1/library/books")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void borrowBookToReader_should_borrow_free_book_to_valid_reader() {
        Book expectedBook = expectedBooks.get(2);
        Reader expectedReader = expectedReaders.get(0);
        expectedBook.setReader(Optional.of(expectedReader));
        Mockito.when(libraryService.borrowBookToReader(3L, 1L)).thenReturn(expectedBook);
        RestAssuredMockMvc.post("/api/v1/library/books/3/readers/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(3))//expectedBook.getId()
                .body("title", Matchers.equalTo(expectedBook.getTitle()))
                .body("author", Matchers.equalTo(expectedBook.getAuthor()))
                .body("reader.id", Matchers.equalTo(1))//expectedReader.getId()
                .body("reader.name", Matchers.equalTo(expectedReader.getName()));
    }

    @Test
    void returnBookToLibrary_should_set_field_reader_to_null() {
        Book bookToReturn = book1();
        Mockito.when(libraryService.returnBookToLibrary(1L)).thenReturn(bookToReturn);
        RestAssuredMockMvc.when()
                .delete("/api/v1/library/books/1/readers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(1))
                .body("title", Matchers.equalTo("Java. The Complete Reference. Twelfth Edition"))
                .body("author", Matchers.equalTo("Herbert Schildt"))
                .body("reader", Matchers.equalTo(null));
    }

    @Test
    void showReaderOfBook_should_return_reader() {
        Reader expectedReader = expectedReaders.get(1);//reader2
        Mockito.when(libraryService.getReaderOfBookWithId(1L)).thenReturn(Optional.of(expectedReader));
        RestAssuredMockMvc.get("/api/v1/library/books/1/readers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(expectedReader.getId().intValue()))
                .body("name", Matchers.equalTo(expectedReader.getName()));
    }

    @Test
    void showReaderOfBook_should_return_empty_optional() {
        Mockito.when(libraryService.getReaderOfBookWithId(3L)).thenReturn(Optional.empty());
        RestAssuredMockMvc.get("/api/v1/library/books/3/readers")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(""));
    }

    @Test
    void showReaderOfBook_should_throw_exception() {
        Mockito.when(libraryService.getReaderOfBookWithId(777L))
                .thenThrow(new ServiceLayerException("Book with ID 777 does not exist in DB."));
        RestAssuredMockMvc.get("/api/v1/library/books/777/readers")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", Matchers.equalTo("Service layer Error"))
                .body("details[0]", Matchers.equalTo("Book with ID 777 does not exist in DB."));
    }

    private Book newBook() {
        Book newBook = new Book();
        newBook.setAuthor("Rebecca Serle");
        newBook.setTitle("One Italian Summer");
        newBook.setReader(Optional.empty());
        return newBook;
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

    private Book book3() {
        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Data Structures And Algorithms Made Easy In JAVA");
        book3.setAuthor("Narasimha Karumanchi");
        book3.setReader(Optional.empty());
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

    private Book bookWithInvalidTitle() {
        Book book = new Book();
        book.setTitle("J4va");
        book.setAuthor("Herbert Schildt");
        book.setReader(Optional.empty());
        return book;
    }

    private Book bookWithInvalidAuthor() {
        Book book = new Book();
        book.setTitle("Java. The Complete Reference. Twelfth Edition");
        book.setAuthor("He");
        book.setReader(Optional.empty());
        return book;
    }

}
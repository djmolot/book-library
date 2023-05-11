package company.name.library.controllers;

import company.name.library.entities.Book;
import company.name.library.entities.ErrorResponse;
import company.name.library.entities.Reader;
import company.name.library.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
@Validated
@Tag(name = "Book Controller", description = "Endpoints for managing books in the library")
public class BookController {
    private final LibraryService libraryService;

    @Operation(summary = "Get all books present in the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books was returned successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(example =
                                    "[{\"id\":1," +
                                            "\"title\":\"Java. The Complete Reference. Twelfth Edition\"," +
                                            "\"author\":\"Herbert Schildt\"," +
                                            "\"reader\":null}," +
                                            "{\"id\":2,\"title\":\"Java. An Introduction to Problem Solving & Programming\"," +
                                            "\"author\":\"Walter Savitch\"," +
                                            "\"reader\":null}]"))
                    }
            )
    })
    @GetMapping
    public ResponseEntity<List<Book>> showAllBooks() {
        List<Book> books = libraryService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Add new book to the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book was added to the library",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"id\":1," +
                                            "\"title\":\"Java. The Complete Reference. Twelfth Edition\"," +
                                            "\"author\":\"Herbert Schildt\"," +
                                            "\"reader\":null}"))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid book supplied",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping
    public ResponseEntity<Book> addNewBook(@RequestBody @Schema(description = "New book object",
            example = "{\"author\": \"Herbert Schildt\",\"title\": \"Java. The Complete Reference. Twelfth Edition\"}") Book book) {
        Book bookFromDB = libraryService.addNewBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookFromDB);
    }

    @Operation(summary = "Borrow book with {id} to reader with {id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book was borrowed to reader",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied to method",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping("/{bookId}/readers/{readerId}")
    public ResponseEntity<Book> borrowBookToReader(
            @Parameter(description = "id of book to be borrowed")
            @PathVariable("bookId") @Positive Long bookId,
            @Parameter(description = "id of reader who borrows a book")
            @PathVariable("readerId") @Positive Long readerId) {
        Book book = libraryService.borrowBookToReader(bookId, readerId);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Return book with {id} to the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book was returned to the library",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"id\":1," +
                                    "\"title\":\"Java. The Complete Reference. Twelfth Edition\"," +
                                    "\"author\":\"Herbert Schildt\"," +
                                    "\"reader\":null}"))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid parameter supplied to method",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @DeleteMapping("/{bookId}/readers")
    public ResponseEntity<Book> returnBookToLibrary(
            @Parameter(description = "id of book to be returned")
            @PathVariable("bookId") @Positive Long bookId) {
        Book book = libraryService.returnBookToLibrary(bookId);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Get reader of book with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the reader of book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"id\": 2,\"name\": \"Voski Daniel\",\"books\": null}")) }),
            @ApiResponse(responseCode = "400", description = "Invalid parameter supplied to method",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Book is not borrowed by any reader",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @GetMapping("/{bookId}/readers")
    public ResponseEntity<Reader> showReaderOfBook(
            @Parameter(description = "id of book to find it's reader")
            @PathVariable("bookId") @Positive Long bookId) {
        return libraryService.getReaderOfBookWithId(bookId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

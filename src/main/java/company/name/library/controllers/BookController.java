package company.name.library.controllers;

import company.name.library.entities.Book;
import company.name.library.entities.ErrorResponse;
import company.name.library.entities.Reader;
import company.name.library.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static company.name.library.controllers.ApiDocExamples.*;

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
                            schema = @Schema(example = BOOKS_LIST))
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
                            schema = @Schema(example = ADDED_BOOK))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid book supplied",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = BOOK_TITLE_INVALID))
            })
    })
    @PostMapping
    public ResponseEntity<Book> addNewBook(
            @Valid @RequestBody @Schema(description = "Book object that needs to be added to the library",
            example = BOOK_TO_ADD) Book book) {
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
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = BOOK_ALREADY_BORROWED)) })
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
                            schema = @Schema(example = ADDED_BOOK)) }),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied to method",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = BOOK_IS_NOT_BORROWED)) })
    })
    @DeleteMapping("/{bookId}/readers")
    public ResponseEntity<Book> returnBookToLibrary(
            @Parameter(description = "id of book to be returned")
            @PathVariable("bookId") @Positive Long bookId) {
        Book book = libraryService.returnBookToLibrary(bookId);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Get reader of book with {id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the reader of book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(example = READER_FOUND)) }
            ),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied to method",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = BOOK_DOES_NOT_EXIST)) }
            ),
            @ApiResponse(responseCode = "404", description = "Book is not borrowed by any reader",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(example = "")) }
            )
    })
    @GetMapping("/{bookId}/readers")
    public ResponseEntity<Reader> showReaderOfBook(
            @Parameter(description = "id of book to find it's reader")
            @PathVariable("bookId") @Positive Long bookId) {
        return libraryService.getReaderOfBookWithId(bookId) //Optional<Reader>
                .map(ResponseEntity::ok) //Optional<ResponseEntity<Reader>> with code 200 or empty Optional
                .orElseGet( //ResponseEntity<Reader> with code 200
                    () -> ResponseEntity.notFound().build()); //or ResponseEntity with no body with code 404
    }
}

package company.name.library.controllers;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.service.LibraryService;
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
@RequestMapping("/api/v1/library/books")
@Validated
public class BookController {
    private final LibraryService libraryService;

    @GetMapping
    public ResponseEntity<List<Book>> showAllBooks() {
        List<Book> books = libraryService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Book> addNewBook(@Valid @RequestBody Book book) {
        Book bookFromDB = libraryService.addNewBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookFromDB);
    }

    @PostMapping("/{bookId}/readers/{readerId}")
    public ResponseEntity<Book> borrowBookToReader(@PathVariable("bookId") @Positive Long bookId,
                                   @PathVariable("readerId") @Positive Long readerId) {
        Book book = libraryService.borrowBookToReader(bookId, readerId);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{bookId}/readers")
    public ResponseEntity<Book> returnBookToLibrary(@PathVariable("bookId") @Positive Long bookId) {
        Book book = libraryService.returnBookToLibrary(bookId);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/{bookId}/readers")
    public ResponseEntity<Reader> showReaderOfBook(@PathVariable("bookId") @Positive Long bookId) {
        return libraryService.getReaderOfBookWithId(bookId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

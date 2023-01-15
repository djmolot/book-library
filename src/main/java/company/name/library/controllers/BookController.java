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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/library/books")
@Validated
public class BookController {
    private final LibraryService libraryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> showAllBooks() {
        return libraryService.getAllBooks();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book addNewBook(@Valid @RequestBody Book book) {
        return libraryService.addNewBook(book);
    }

    @PostMapping("/{bookId}/readers/{readerId}")
    @ResponseStatus(HttpStatus.OK)
    public Book borrowBookToReader(@PathVariable("bookId") @Positive Long bookId,
                                   @PathVariable("readerId") @Positive Long readerId) {
        return libraryService.borrowBookToReader(bookId, readerId);
    }

    @DeleteMapping("/{bookId}/readers")
    @ResponseStatus(HttpStatus.OK)
    public Book returnBookToLibrary(@PathVariable("bookId") @Positive Long bookId) {
        return libraryService.returnBookToLibrary(bookId);
    }

    @GetMapping("/{bookId}/readers")
    public ResponseEntity<Reader> showReaderOfBook(@PathVariable("bookId") @Positive Long bookId) {
        return libraryService.getReaderOfBookWithId(bookId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

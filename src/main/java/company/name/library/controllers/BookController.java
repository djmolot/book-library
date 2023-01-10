package company.name.library.controllers;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/library/books")
public class BookController {
    private final LibraryService libraryService;

    public BookController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

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
    public Book borrowBookToReader(@PathVariable Long bookId, @PathVariable Long readerId) {
        return libraryService.borrowBookToReader(bookId, readerId);
    }

    @DeleteMapping("/{bookId}/readers")
    @ResponseStatus(HttpStatus.OK)
    public Book returnBookToLibrary(@PathVariable Long bookId) {
        return libraryService.returnBookToLibrary(bookId);
    }

    @GetMapping("/{bookId}/readers")
    public ResponseEntity<Reader> showReaderOfBook(@PathVariable Long bookId) {
        Optional<Reader> readerOptional = libraryService.getReaderOfBookWithId(bookId);
        return readerOptional.map(reader -> new ResponseEntity<>(reader, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

}

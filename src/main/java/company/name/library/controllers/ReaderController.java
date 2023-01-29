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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/library/readers")
@Validated
public class ReaderController {
    private final LibraryService libraryService;

    @GetMapping
    public ResponseEntity<List<Reader>> showAllReaders() {
        List<Reader> readers = libraryService.getAllReaders();
        return ResponseEntity.ok(readers);
    }

    @PostMapping
    public ResponseEntity<Reader> registerNewReader(@Valid @RequestBody Reader reader) {
        Reader readerFromDB = libraryService.registerNewReader(reader);
        return ResponseEntity.status(HttpStatus.CREATED).body(readerFromDB);
    }

    @GetMapping("/{readerId}/books")
    public ResponseEntity<List<Book>> showAllBooksBorrowedByReader(
            @PathVariable("readerId") @Positive Long readerId) {
        List<Book> books = libraryService.getAllBooksOfReader(readerId);
        return ResponseEntity.ok(books);
    }

}

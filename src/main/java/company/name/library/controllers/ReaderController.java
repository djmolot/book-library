package company.name.library.controllers;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.service.LibraryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/v1/library/readers")
@Validated
public class ReaderController {
    private final LibraryService libraryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Reader> showAllReaders() {
        return libraryService.getAllReaders();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reader registerNewReader(@Valid @RequestBody Reader reader) {
        return libraryService.registerNewReader(reader);
    }

    @GetMapping("/{readerId}/books")
    @ResponseStatus(HttpStatus.OK)
    List<Book> showAllBooksBorrowedByReader(@PathVariable("readerId") @Positive Long readerId) {
        return libraryService.getAllBooksOfReader(readerId);
    }

}

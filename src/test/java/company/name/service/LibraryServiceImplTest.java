package company.name.service;

import company.name.entities.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceImplTest {
    private LibraryService libraryService = new LibraryServiceImpl();

    @org.junit.jupiter.api.Test
    void getAllBooks_ok() {
        List<Book> allBooks = libraryService.getAllBooks();
        boolean actual = allBooks.isEmpty();
        assertFalse(actual);
    }

    @org.junit.jupiter.api.Test
    void getAllBooks_not_ok() {
        List<Book> allBooks = libraryService.getAllBooks();
        if (allBooks == null) {
            fail();
        }
    }

}
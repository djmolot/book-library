package company.name.library.service;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.ServiceLayerException;
import company.name.library.repositories.BookRepository;
import company.name.library.repositories.ReaderRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LibraryServiceImpl implements LibraryService {
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    @Override
    public List<Reader> getAllReaders() {
        return readerRepository.getAll();
    }

    @Override
    public Reader registerNewReader(String input) {
        validateInputNewReader(input);
        Reader reader = new Reader();
        reader.setName(input);
        return readerRepository.add(reader);
    }

    @Override
    public Book addNewBook(Book book) {
        return bookRepository.add(book);
    }

    @Override
    public Book borrowBookToReader(Long bookId, Long readerId) {
        Reader reader = readerRepository.getById(readerId).orElseThrow(
                () -> new ServiceLayerException("Reader with ID " + readerId
                        + " does not exist in DB.")
        );
        Book book = bookRepository.getById(bookId).orElseThrow(
                () -> new ServiceLayerException("Book with ID " + bookId
                        + " does not exist in DB.")
        );
        book.getReader().ifPresent(
                r -> {
                    throw new ServiceLayerException("Book with ID " + bookId
                            + " has already borrowed by reader " + r + ".");
                }
        );
        book.setReader(Optional.of(reader));
        return bookRepository.update(book);
    }

    @Override
    public Book returnBookToLibrary(Long bookId) {
        Book book = bookRepository.getById(bookId).orElseThrow(
                () -> new ServiceLayerException("Book with ID " + bookId + " does not exist in DB.")
        );
        book.setReader(Optional.empty());
        return bookRepository.update(book);
    }

    @Override
    public List<Book> getAllBooksOfReader(String input) {
        validateInputGetAllBooksOfReader(input);
        Long readerId;
        try {
            readerId = Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new ServiceLayerException("The string " + input
                    + " does not contain a parsable long. " + e.getMessage());
        }
        readerRepository.getById(readerId).orElseThrow(
                () -> new ServiceLayerException("Reader with ID " + readerId
                        + " does not exist in DB.")
        );
        return bookRepository.getBooksByReaderId(readerId);
    }

    @Override
    public Optional<Reader> getReaderOfBookWithId(Long bookId) {
        bookRepository.getById(bookId).orElseThrow(
                () -> new ServiceLayerException("Book with ID " + bookId + " does not exist in DB.")
        );
        return readerRepository.getReaderByBookId(bookId);
    }

    private void validateInputNewReader(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("Reader name can't be null or empty.");
        }
        if (StringUtils.countMatches(input, '/') > 0) {
            throw new ServiceLayerException("Reader name can't contain symbol '/'.");
        }
    }

    private void validateInputGetAllBooksOfReader(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("getAllBooksOfReader input can't be null or empty.");
        }
    }

}

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
    public Reader registerNewReader(Reader reader) {
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
        return bookRepository.update(book).orElseThrow(
                () -> new ServiceLayerException("Failed to update book with ID " + bookId)
        );
    }

    @Override
    public Book returnBookToLibrary(Long bookId) {
        Book book = bookRepository.getById(bookId).orElseThrow(
                () -> new ServiceLayerException("Book with ID " + bookId + " does not exist in DB.")
        );
        if (book.getReader().isEmpty()) {
            throw  new ServiceLayerException("Book with ID " + bookId + " does not borrowed by any reader.");
        }
        book.setReader(Optional.empty());
        return bookRepository.update(book).orElseThrow(
                () -> new ServiceLayerException("Failed to update book with ID " + bookId)
        );
    }

    @Override
    public List<Book> getAllBooksOfReader(Long readerId) {
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
}

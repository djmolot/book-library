package company.name.library.service;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import company.name.library.exceptions.ServiceLayerException;
import company.name.library.repositories.BookRepository;
import company.name.library.repositories.ReaderRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LibraryServiceImpl implements LibraryService {
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    @Value("${library.maxNumberOfBooksToBorrow}")
    private int maxNumberOfBooksToBorrow;
    @Value("${library.minAgeOfReaderForRestricted}")
    private int minAgeOfReaderForRestricted;

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
        if (book.getReader().isPresent() || book.getBorrowDate().isPresent()) {
            throw new ServiceLayerException("Cannot set reader or borrowDate when adding a new book.");
        }
        return bookRepository.add(book);
    }

    @Override
    public Book borrowBookToReader(Long bookId, Long readerId) {
        Book book = getBookIfPresentOrThrow(bookId);
        book.getReader().ifPresent(r -> {
            throw new ServiceLayerException("Book with ID " + bookId + " has already borrowed by reader "
                    + "id:" + r.getId() + " name:" + r.getName() + ".");
        });
        Reader reader = getReaderIfPresentOrThrow(readerId);
        List<Book> readerBooks = bookRepository.getBooksByReaderId(readerId);
        reader.setBooks(readerBooks);
        validateNumberOfBorrowedBooksByReader(reader);
        validateBookHoldingPeriodInBooksOfReader(reader);
        if(book.isRestricted()) {
            validateReaderAge(reader);
        }
        reader.setBooks(null);
        book.setReader(Optional.of(reader));
        book.setBorrowDate(Optional.of(LocalDate.now()));
        return bookRepository.updateBorrowDetails(book);
    }

    @Override
    public Book returnBookToLibrary(Long bookId) {
        Book book = getBookIfPresentOrThrow(bookId);
        book.getReader().orElseThrow(() ->
                new ServiceLayerException("Book with ID " + bookId + " is not borrowed by any reader.")
        );
        book.setReader(Optional.empty());
        book.setBorrowDate(Optional.empty());
        return bookRepository.updateBorrowDetails(book);
    }

    @Override
    public List<Book> getAllBooksOfReader(Long readerId) {
        getReaderIfPresentOrThrow(readerId);
        return bookRepository.getBooksByReaderId(readerId);
    }

    @Override
    public Optional<Reader> getReaderOfBookWithId(Long bookId) {
        getBookIfPresentOrThrow(bookId);
        return readerRepository.getReaderByBookId(bookId);
    }

    private Reader getReaderIfPresentOrThrow(Long readerId) {
        return readerRepository.getById(readerId).orElseThrow(
                () -> new ServiceLayerException("Reader with ID " + readerId
                        + " does not exist in DB.")
        );
    }

    private Book getBookIfPresentOrThrow(Long bookId) {
        return bookRepository.getById(bookId).orElseThrow(
                () -> new ServiceLayerException("Book with ID " + bookId
                        + " does not exist in DB.")
        );
    }

    private void validateNumberOfBorrowedBooksByReader(Reader reader) {
        int booksBorrowed = reader.getBooks().size();
        if(booksBorrowed < maxNumberOfBooksToBorrow) {
            return;
        } else {
            throw new ServiceLayerException("Reader has already borrowed " + booksBorrowed +
                    " books, maximum number is " + maxNumberOfBooksToBorrow);
        }
    }

    private void validateBookHoldingPeriodInBooksOfReader(Reader reader) {
        List<Book> booksOfReader = reader.getBooks();
        booksOfReader.forEach(book -> {
            book.getBorrowDate().ifPresent(borrowDate -> {
                long holdingPeriod = ChronoUnit.DAYS.between(borrowDate, LocalDate.now());
                if (holdingPeriod > book.getMaxBorrowTimeInDays()) {
                    throw new ServiceLayerException("Book id:" + book.getId() + " \"" + book.getTitle()
                            + "\" holding period " + holdingPeriod +
                            " exceeds maximum borrow time in days " + book.getMaxBorrowTimeInDays());
                }
            });
        });
    }

    private void validateReaderAge(Reader reader) {
        LocalDate currentDate = LocalDate.now();
        LocalDate readerBirthDate = reader.getBirthDate();
        int readerAge = Period.between(readerBirthDate, currentDate).getYears();
        if (readerAge < minAgeOfReaderForRestricted) {
            throw new ServiceLayerException("Reader id:" + reader.getId() + " " + reader.getName() +
                    " age:" + readerAge + " is not old enough to borrow restricted books");
        }
    }

}

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
        return bookRepository.add(book);
    }

    @Override
    public Book borrowBookToReader(Long bookId, Long readerId) {
        Reader reader = readerRepository.getById(readerId).orElseThrow(
                () -> new ServiceLayerException("Reader with ID " + readerId
                        + " does not exist in DB.")
        );
        List<Book> readerBooks = bookRepository.getBooksByReaderId(readerId);
        reader.setBooks(readerBooks);
        Book book = bookRepository.getById(bookId).orElseThrow(
                () -> new ServiceLayerException("Book with ID " + bookId
                        + " does not exist in DB.")
        );
        validateNumberOfBorrowedBooksByReader(reader);
        validateBookHoldingPeriodInBooksOfReader(reader);
        if(book.getRestricted()) {
            validateReaderAge(reader);
        }
        book.getReader().ifPresent(
                r -> {
                    throw new ServiceLayerException("Book with ID " + bookId
                            + " has already borrowed by reader " + r + ".");
                }
        );
        book.setReader(Optional.of(reader));
        book.setBorrowDate(Optional.of(LocalDate.now()));
        return bookRepository.update(book);
    }

    @Override
    public Book returnBookToLibrary(Long bookId) {
        Book book = bookRepository.getById(bookId).orElseThrow(
                () -> new ServiceLayerException("Book with ID " + bookId + " does not exist in DB.")
        );
        if (book.getReader().isEmpty()) {
            throw  new ServiceLayerException("Book with ID " + bookId + " is not borrowed by any reader.");
        }
        book.setReader(Optional.empty());
        book.setBorrowDate(Optional.empty());
        return bookRepository.update(book);
    }

    @Override
    public List<Book> getAllBooksOfReader(Long readerId) {
        readerRepository.getById(readerId).orElseThrow(
                () -> new ServiceLayerException("Reader with ID " + readerId + " does not exist in DB.")
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
        LocalDate now = LocalDate.now();
        for (Book book : booksOfReader) {
            Optional<LocalDate> borrowDateOptional = book.getBorrowDate();
            if (borrowDateOptional.isPresent()) {
                LocalDate borrowDate = borrowDateOptional.get();
                long holdingPeriod = ChronoUnit.DAYS.between(borrowDate, now);
                int maxBorrowTimeInDays = book.getMaxBorrowTimeInDays();
                if (holdingPeriod > maxBorrowTimeInDays) {
                    throw new ServiceLayerException("Book " + book.getTitle() + " holding period " + holdingPeriod +
                            " exceeds maximum borrow time in days " + maxBorrowTimeInDays);
                }
            }
        }
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

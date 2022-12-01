package company.name.service;

import company.name.dao.BookDao;
import company.name.dao.ReaderDao;
import company.name.entities.Book;
import company.name.entities.Reader;
import company.name.exceptions.ServiceLayerException;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class LibraryServiceImpl implements LibraryService {
    private final BookDao bookDao;
    private final ReaderDao readerDao;

    public LibraryServiceImpl(BookDao bookDao, ReaderDao readerDao) {
        this.bookDao = bookDao;
        this.readerDao = readerDao;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }

    @Override
    public List<Reader> getAllReaders() {
        return readerDao.getAll();
    }

    @Override
    public Reader registerNewReader(String input) {
        validateInputNewReader(input);
        Reader reader = new Reader();
        reader.setName(input);
        return readerDao.add(reader);
    }

    @Override
    public Book addNewBook(String input) {
        validateInputNewBook(input);
        String[] splittedInput = input.split("/");
        String bookTitle = splittedInput[0];
        String bookAuthor = splittedInput[1];
        Book book = new Book();
        book.setTitle(bookTitle);
        book.setAuthor(bookAuthor);
        return bookDao.add(book);
    }

    @Override
    public void borrowBookToReader(String input) {
        validateInputBorrowBookToReader(input);
        String[] splittedInput = input.split("/");
        final Long bookId;
        final Long readerId;
        try {
            bookId = Long.parseLong(splittedInput[0]);
            readerId = Long.parseLong(splittedInput[1]);
        } catch (NumberFormatException e) {
            throw new ServiceLayerException("The string does not contain a parsable long. "
                    + e.getMessage());
        }
        Reader reader = readerDao.getById(readerId).orElseThrow(
                () -> new ServiceLayerException("Reader with ID " + readerId
                        + " does not exist in DB.")
        );
        Book book = bookDao.getById(bookId).orElseThrow(
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
        bookDao.update(book);
    }

    @Override
    public void returnBookToLibrary(String input) {
        validateInputReturnBookToLibrary(input);
        Long bookId;
        try {
            bookId = Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new ServiceLayerException("The string " + input
                    + " does not contain a parsable long. " + e.getMessage());
        }
        Book book = bookDao.getById(bookId).orElseThrow(
                () -> new ServiceLayerException("Book with ID " + bookId + " does not exist in DB.")
        );
        book.setReader(Optional.empty());
        bookDao.update(book);
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
        readerDao.getById(readerId).orElseThrow(
                () -> new ServiceLayerException("Reader with ID " + readerId
                        + " does not exist in DB.")
        );
        return bookDao.getBooksByReaderId(readerId);
    }

    @Override
    public Optional<Reader> getReaderOfBookWithId(String input) {
        validateInputGetReaderOfBookWithId(input);
        Long bookId;
        try {
            bookId = Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new ServiceLayerException("The string " + input
                    + " does not contain a parsable long. " + e.getMessage());
        }
        bookDao.getById(bookId).orElseThrow(
                () -> new ServiceLayerException("Book with ID " + bookId + " does not exist in DB.")
        );
        return readerDao.getReaderByBookId(bookId);
    }

    private void validateInputNewReader(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("Reader name can't be null or empty.");
        }
        if (StringUtils.countMatches(input, '/') > 0) {
            throw new ServiceLayerException("Reader name can't contain symbol '/'.");
        }
    }

    private void validateInputNewBook(String input) {
        if (input == null || input.length() < 3) {
            throw new ServiceLayerException(
                    "New book input can't be null and should contain at least 1 symbol "
                            + "for both name and author.");
        } else if (StringUtils.countMatches(input, '/') != 1) {
            throw new ServiceLayerException("New book input must contain one '/' character.");
        }
        String[] splittedInput = input.split("/");
        if (splittedInput.length != 2 || StringUtils.isAnyBlank(splittedInput)) {
            throw new ServiceLayerException("New book input is not valid.");
        }
    }

    private void validateInputBorrowBookToReader(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("BorrowBookToReader input can't be null or empty.");
        } else if (StringUtils.countMatches(input, '/') != 1) {
            throw new ServiceLayerException(
                    "BorrowBookToReader input must contain one '/' character.");
        }
    }

    private void validateInputReturnBookToLibrary(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("ReturnBookToLibrary input can't be null or empty.");
        }
    }

    private void validateInputGetAllBooksOfReader(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("getAllBooksOfReader input can't be null or empty.");
        }
    }

    private void validateInputGetReaderOfBookWithId(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("getReaderOfBookWithId input can't be null or empty.");
        }
    }

}

package company.name.service;

import company.name.dao.BookDao;
import company.name.dao.BookDaoPostgreSqlImpl;
import company.name.dao.ReaderDao;
import company.name.dao.ReaderDaoPostgreSqlImpl;
import company.name.exceptions.DaoLayerException;
import company.name.exceptions.ServiceLayerException;
import company.name.models.Book;
import company.name.models.Reader;
import java.util.List;
import java.util.NoSuchElementException;

public class LibraryServiceImpl implements LibraryService {
    private final BookDao bookDao = new BookDaoPostgreSqlImpl();
    private final ReaderDao readerDao = new ReaderDaoPostgreSqlImpl();

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
                () -> new DaoLayerException("Reader with ID " + readerId + " does not exist in DB.")
        );
        Book book = bookDao.getById(bookId).orElseThrow(
                () -> new DaoLayerException("Book with ID " + bookId + " does not exist in DB.")
        );
        if (book.getReader() != null) {
            throw new ServiceLayerException("Book with ID " + bookId
                    + " has already borrowed by reader " + book.getReader());
        }
        book.setReader(reader);
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
        bookDao.getById(bookId).ifPresent(
                book -> {
                    book.setReader(null);
                    bookDao.update(book);
                }
        );
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
        return bookDao.getBooksByReaderId(readerId);
    }

    @Override
    public Reader getReaderOfBookWithId(String input) {
        validateInputGetReaderOfBookWithId(input);
        Long bookId;
        try {
            bookId = Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new ServiceLayerException("The string " + input
                    + " does not contain a parsable long. " + e.getMessage());
        }
        try {
            return readerDao.getReaderByBookId(bookId).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new DaoLayerException("Book with ID " + bookId
                    + " is not borrowed. No reader to show. " + e.getMessage());
        }
    }

    @Override
    public void prepareLibraryData() {
        if (bookDao.getAll().size() == 0 || readerDao.getAll().size() == 0) {
            Reader reader1 = new Reader();
            reader1.setName("Zhirayr Hovik");
            readerDao.add(reader1);

            Reader reader2 = new Reader();
            reader2.setName("Voski Daniel");
            readerDao.add(reader2);

            Reader reader3 = new Reader();
            reader3.setName("Ruben Nazaret");
            readerDao.add(reader3);

            Book book1 = new Book();
            book1.setAuthor("Herbert Schildt");
            book1.setTitle("Java. The Complete Reference. Twelfth Edition");
            readerDao.getById(1L).ifPresent(book1::setReader);
            bookDao.add(book1);

            Book book2 = new Book();
            book2.setAuthor("Walter Savitch");
            book2.setTitle("Java. An Introduction to Problem Solving & Programming");
            readerDao.getById(2L).ifPresent(book2::setReader);
            bookDao.add(book2);

            Book book3 = new Book();
            book3.setAuthor("Narasimha Karumanchi");
            book3.setTitle("Data Structures And Algorithms Made Easy In JAVA");
            readerDao.getById(3L).ifPresent(book3::setReader);
            bookDao.add(book3);
        }
    }

    private void validateInputNewReader(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("Reader name can't be null or empty.");
        }
        if (countCharOccurrencesInString(input, '/') > 0) {
            throw new ServiceLayerException("Reader name can't contain symbol '/'.");
        }
    }

    private void validateInputNewBook(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("New book input can't be null or empty.");
        } else if (countCharOccurrencesInString(input, '/') != 1) {
            throw new ServiceLayerException("New book input must contain one '/' character.");
        } else if (countCharOccurrencesInString(input, '/') == 1 && input.length() == 1) {
            throw new ServiceLayerException("New book input '/' is not valid here.");
        }
    }

    private void validateInputBorrowBookToReader(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("BorrowBookToReader input can't be null or empty.");
        } else if (countCharOccurrencesInString(input, '/') != 1) {
            throw new ServiceLayerException(
                    "BorrowBookToReader input must contain one '/' character.");
        }
    }

    private void validateInputReturnBookToLibrary(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("ReturnBookToLibrary input can't be null or empty.");
        }
        if (countCharOccurrencesInString(input, '/') > 0) {
            throw new ServiceLayerException("ReturnBookToLibrary input can't contain symbol '/'.");
        }
    }

    private void validateInputGetAllBooksOfReader(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("GetAllBooksOfReader input can't be null or empty.");
        }
        if (countCharOccurrencesInString(input, '/') > 0) {
            throw new ServiceLayerException("GetAllBooksOfReader input can't contain symbol '/'.");
        }
    }

    private void validateInputGetReaderOfBookWithId(String input) {
        if (input == null || input.length() == 0) {
            throw new ServiceLayerException("GetReaderOfBookWithId input can't be null or empty.");
        }
        if (countCharOccurrencesInString(input, '/') > 0) {
            throw new ServiceLayerException(
                    "GetReaderOfBookWithId input can't contain symbol '/'.");
        }
    }

    private long countCharOccurrencesInString(String string, char character) {
        return string.chars()
                .filter(ch -> ch == character).count();
    }

}

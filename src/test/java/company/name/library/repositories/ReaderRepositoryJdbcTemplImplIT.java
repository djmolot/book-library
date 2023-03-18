package company.name.library.repositories;

import company.name.library.TestDataProducer;
import company.name.library.entities.Reader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class ReaderRepositoryJdbcTemplImplIT {
    @Autowired
    private ReaderRepository readerRepository;

    @Test
    void add_should_return_added_reader_with_empty_books_list() {
        Reader newReader = TestDataProducer.newReader();
        String expectedName = newReader.getName();
        Assertions.assertNull(newReader.getId(),
                "Id of new reader must be null");
        Assertions.assertTrue(newReader.getBooks().isEmpty(),
                "Books of new reader must be empty list");

        Reader savedReader = readerRepository.add(newReader);
        Assertions.assertNotNull(savedReader.getId(),
                "ID of saved reader should not be null.");
        Assertions.assertEquals(expectedName, savedReader.getName(),
                "Name of saved reader should be equal to expectedName");
        Assertions.assertTrue(savedReader.getBooks().isEmpty(),
                "Books of saved reader must be empty list");
    }

    @Test
    void getById_should_return_existing_reader_with_books_that_was_not_initialized() {
        Long readerId = 2L;
        Reader expectedReader2 = TestDataProducer.newReader2();
        Assertions.assertNull(expectedReader2.getBooks(),
                "field books of expectedReader2 should be not initialized");
        Reader actualReader2 = readerRepository.getById(readerId).orElse(null);
        Assertions.assertNotNull(actualReader2,"Method should not return empty Optional.");
        Assertions.assertEquals(expectedReader2, actualReader2,
                "actualReader2 should be equal to expectedReader2");
    }

    @Test
    void getById_should_return_empty_optional_if_reader_with_this_id_does_not_exist_in_DB() {
        Long readerId = 777L;
        Optional<Reader> readerOptional = readerRepository.getById(readerId);
        Assertions.assertTrue(readerOptional.isEmpty(),
                "getById() should return empty Optional if Reader with this Id does not exist in DB");
    }

    @Test
    void getAll_should_return_list_equal_to_expected() {
        List<Reader> expectedReaders = TestDataProducer.newAllReadersList();
        List<Reader> actualReaders = readerRepository.getAll();
        Assertions.assertNotNull(actualReaders,
                "actualReaders should not be null");
        Assertions.assertEquals(expectedReaders, actualReaders,
                "actualReaders should be equal to expectedReaders");
    }

    @Test
    void getReaderByBookId_should_return_reader_witn_not_initialized_books_field() {
        Long bookId = 1L;
        Reader expectedReader2 = TestDataProducer.newReader2();
        Assertions.assertNull(expectedReader2.getBooks(),
                "field books of expectedReader2 should be not initialized");
        Reader actualReader2 = readerRepository.getReaderByBookId(bookId).orElse(null);
        Assertions.assertNotNull(actualReader2, "actualReader should not be null");
        Assertions.assertEquals(expectedReader2, actualReader2,
                "actualReader2 should be equal to expectedReader2");
    }

    @Test
    void getReaderByBookId_should_return_empty_optional_for_book_wich_is_not_borrowed() {
        Long bookId = 3L;
        Optional<Reader> readerOptional = readerRepository.getReaderByBookId(bookId);
        Assertions.assertTrue(readerOptional.isEmpty(),
                "readerOptional of book3 should be empty");
    }

}
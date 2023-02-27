package company.name.library.repositories;

import company.name.library.TestDatabaseData;
import company.name.library.entities.Reader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private final ReaderRepository readerRepository;
    private final TestDatabaseData testDatabaseData = new TestDatabaseData();
    private List<Reader> expectedReaders;

    @Autowired
    ReaderRepositoryJdbcTemplImplIT(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    @BeforeEach
    public void setUp() {
        expectedReaders = testDatabaseData.getTestReaders();
    }

    @Test
    void add_should_return_added_reader() {
        Reader expectedReader = testDatabaseData.newReader();
        Reader actualReader = readerRepository.add(expectedReader);
        Assertions.assertNotNull(expectedReader.getId(),"ID of added Reader should not be Null.");
        Assertions.assertEquals(expectedReader, actualReader, "ActualReader should be equal to ExpectedReader");
    }

    @Test
    void getById_should_return_existing_reader() {
        Reader expectedReader2 = testDatabaseData.reader2();//reader2 without books
        Reader actualReader2 = readerRepository.getById(2L).orElse(null);
        Assertions.assertNotNull(actualReader2,"Method should not return empty Optional.");
        Assertions.assertEquals(expectedReader2, actualReader2, "ActualReader should be equal to ExpectedReader");
    }

    @Test
    void getById_should_return_empty_optional_if_reader_with_this_id_does_not_exist_in_DB() {
        Optional<Reader> readerOpt = readerRepository.getById(777L);
        Assertions.assertTrue(readerOpt.isEmpty(),
                "getById() should return empty Optional if Reader with this Id does not exist in DB.");
    }

    @Test
    void getAll_should_return_list_equal_to_expected() {
        List<Reader> actualReaders = readerRepository.getAll();
        Assertions.assertEquals(expectedReaders, actualReaders, "actualReaders should be equal to expectedReaders");
    }

    @Test
    void getReaderByBookId_should_return_reader() {
        Reader expectedReader2 = testDatabaseData.reader2();
        Reader actualReader2 = readerRepository.getReaderByBookId(1L).orElse(null);
        Assertions.assertNotNull(actualReader2, "actualReader should not be null");
        Assertions.assertEquals(expectedReader2, actualReader2,
                "actualReader should be equal to expectedReader");
    }

    @Test
    void getReaderByBookId_should_return_empty_optional() {
        Optional<Reader> readerOptional = readerRepository.getReaderByBookId(3L);
        Assertions.assertTrue(readerOptional.isEmpty(), "readerOptional should be empty");
    }

}
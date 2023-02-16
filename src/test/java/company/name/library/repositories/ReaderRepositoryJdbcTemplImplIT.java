package company.name.library.repositories;

import company.name.library.entities.Book;
import company.name.library.entities.Reader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class ReaderRepositoryJdbcTemplImplIT {
    private final ReaderRepository readerRepository;
    private final List<Reader> expectedReaders;

    @Autowired
    ReaderRepositoryJdbcTemplImplIT(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
        this.expectedReaders = expectedReaders();
    }

    @Test
    void add_should_return_added_reader() {
        Reader expectedReader = newReader();
        Reader actualReader = readerRepository.add(expectedReader);
        Assertions.assertNotNull(expectedReader.getId(),"ID of added Reader should not be Null.");
        Assertions.assertEquals(expectedReader, actualReader, "ActualReader should be equal to ExpectedReader");
    }

    @Test
    void getById_should_return_existing_reader() {
        Reader expectedReader2 = expectedReaders.get(1);
        Reader actualReader2 = readerRepository.getById(2L).orElse(null);
        Assertions.assertNotNull(actualReader2,"Method should not return empty Optional.");
        Assertions.assertEquals(expectedReader2, actualReader2, "ActualReader should be equal to ExpectedReader");
    }

    @Test
    void getById_should_return_empty_Optional_if_Reader_with_this_Id_does_not_exist_in_DB() {
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
        Reader expectedReader2WithoutBooks = reader2();
        Reader actualReader2WithoutBooks = readerRepository.getReaderByBookId(1L).orElse(null);
        Assertions.assertNotNull(actualReader2WithoutBooks, "actualReader should not be null");
        Assertions.assertEquals(expectedReader2WithoutBooks, actualReader2WithoutBooks,
                "actualReader should be equal to expectedReader");
    }

    @Test
    void getReaderByBookId_should_return_empty_optional() {
        Optional<Reader> readerOptional = readerRepository.getReaderByBookId(3L);
        Assertions.assertTrue(readerOptional.isEmpty(), "readerOptional should be empty");
    }

    private Book book1() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Java. The Complete Reference. Twelfth Edition");
        book1.setAuthor("Herbert Schildt");
        return book1;
    }

    private Book book2() {
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Java. An Introduction to Problem Solving & Programming");
        book2.setAuthor("Walter Savitch");
        return book2;
    }

    private Book book3() {
        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Data Structures And Algorithms Made Easy In JAVA");
        book3.setAuthor("Narasimha Karumanchi");
        return book3;
    }

    private Reader reader1() {
        Reader reader1 = new Reader();
        reader1.setId(1L);
        reader1.setName("Zhirayr Hovik");
        return reader1;
    }

    private Reader reader2() {
        Reader reader2 = new Reader();
        reader2.setId(2L);
        reader2.setName("Voski Daniel");
        return reader2;
    }

    private Reader reader3() {
        Reader reader3 = new Reader();
        reader3.setId(3L);
        reader3.setName("Ruben Nazaret");
        return reader3;
    }

    private Reader newReader() {
        Reader newReader = new Reader();
        newReader.setName("Addy Morra");
        return newReader;
    }

    private List<Reader> expectedReaders() {
        Reader reader1 = reader1();
        reader1.setBooks(new ArrayList<>());
        Reader reader2 = reader2();
        reader2.setBooks(List.of(book1(), book2()));
        Reader reader3 = reader3();
        reader3.setBooks(new ArrayList<>());
        return List.of(reader1, reader2, reader3);
    }
}
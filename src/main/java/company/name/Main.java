package company.name;

import company.name.dao.BookDao;
import company.name.dao.BookDaoPostgreSqlImpl;
import company.name.dao.ReaderDao;
import company.name.dao.ReaderDaoPostgreSqlImpl;
import company.name.service.LibraryService;
import company.name.service.LibraryServiceImpl;
import company.name.ui.Application;

public class Main {

    public static void main(String[] args) {

        BookDao bookDao = new BookDaoPostgreSqlImpl();
        ReaderDao readerDao = new ReaderDaoPostgreSqlImpl();
        LibraryService libraryService = new LibraryServiceImpl(bookDao, readerDao);

        Application application = new Application(libraryService);
        application.run();
    }

}

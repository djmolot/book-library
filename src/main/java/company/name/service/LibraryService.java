package company.name.service;

public interface LibraryService {
    void showAllBooks();

    void showAllReaders();

    void registerNewReader();

    void addNewBook();

    void borrowBookToReader();

    void returnBookToLibrary();

    void showAllBooksOfReader();

    void showReaderOfBook();

    void prepareLibraryData();
}

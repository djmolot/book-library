package company.name.library.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private final String defaultMaxBorrowTimeInDays;
    private final String maxNumberOfBooksToBorrow;
    private final String minAgeOfReaderForRestricted;

    public AppConfig() {
        this.defaultMaxBorrowTimeInDays = System.getenv("BOOK_LIBRARY__DEFAULT_MAX_BORROW_TIME_IN_DAYS");
        this.maxNumberOfBooksToBorrow = System.getenv("BOOK_LIBRARY__MAXIMUM_NUMBER_OF_BOOKS_FOR_A_READER_TO_BORROW_SIMULTANEOUSLY");
        this.minAgeOfReaderForRestricted = System.getenv("BOOK_LIBRARY__MIMIMUM_AGE_OF_A_READER_FOR_BORROWING_RESTRICTED_BOOKS");
    }

    public String getDefaultMaxBorrowTimeInDays() {
        return defaultMaxBorrowTimeInDays;
    }

    public String getMaxNumberOfBooksToBorrow() {
        return maxNumberOfBooksToBorrow;
    }

    public String getMinAgeOfReaderForRestricted() {
        return minAgeOfReaderForRestricted;
    }

}

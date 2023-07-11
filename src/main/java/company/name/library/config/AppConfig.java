package company.name.library.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@PropertySource("classpath:application.yml")
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

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration")
                .placeholders(Collections.singletonMap("defaultMaxBorrowTimeInDays", getDefaultMaxBorrowTimeInDays()))
                .load();
        flyway.migrate();
        return flyway;
    }

}

package company.name.library.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
public class AppConfig2 {

    private final Environment environment;

    @Autowired
    public AppConfig2(Environment environment) {
        this.environment = environment;
    }

    public int getDefaultMaxBorrowTimeInDays() {
        return Integer.parseInt(environment.getProperty("BOOK_LIBRARY__DEFAULT_MAX_BORROW_TIME_IN_DAYS"));
    }

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration")
                .placeholders(Collections.singletonMap("defaultMaxBorrowTimeInDays", String.valueOf(getDefaultMaxBorrowTimeInDays())))
                .load();
        flyway.migrate();
        return flyway;
    }
}

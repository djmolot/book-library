package company.name.library.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
public class WelcomeController {

    @GetMapping("/api/v1/library/welcome")
    public Map<String, String> sayWelcome() {
        return Map.of(
                "message", "Welcome to the library!",
                "currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                "currentTime", LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME)
        );
    }
}

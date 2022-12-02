package company.name.library.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class WelcomeController {
    @GetMapping("/api/v1/library/welcome")
    public Map<String, String> sayWelcome() {
        Map<String, String> responseMap = new LinkedHashMap<>();
        responseMap.put("message", "Welcome to the library!");
        responseMap.put("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return responseMap;
    }
}

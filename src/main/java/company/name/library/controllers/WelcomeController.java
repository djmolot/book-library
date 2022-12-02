package company.name.library.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class WelcomeController {
    @GetMapping("/api/v1/library/welcome")
    //@RequestMapping(value = "/api/v1/library/welcome", method = RequestMethod.GET,
            //produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> sayWelcome() {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("message", "Welcome to the library!");
        responseMap.put("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return responseMap;
    }
}

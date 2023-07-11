package company.name.library.controllers;

import company.name.library.config.AppConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import static company.name.library.controllers.ApiDocExamples.WELCOME_OBJECT;

@RequiredArgsConstructor
@RestController
public class WelcomeController {
    private final AppConfig appConfig;

    @Operation(summary = "Sends welcome request to server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request was successful",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(example = WELCOME_OBJECT)) }
            )
    })
    @GetMapping("/api/v1/welcome")
    public LinkedHashMap<String, String> sayWelcome() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put("message", "Welcome to the library!");
        response.put("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        response.put("currentTime", LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
        response.put("DefaultMaxBorrowTimeInDays", appConfig.getDefaultMaxBorrowTimeInDays());
        return response;
    }
}

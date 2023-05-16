package company.name.library.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/*
Special class which will be returned for all failure cases.
Having consistent error message structure for all APIs,
help the API consumers to write more robust code.
 */

@RequiredArgsConstructor
@Getter
public class ErrorResponse {

    // "10-01-2023 17:13:41"
    private final String dateTime;

    //General error message about nature of error
    private final String errorMessage;

    //Specific errors in API request processing
    private final List<ApiError> errors;

}

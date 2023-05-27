package company.name.library.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.util.List;

/*
Special class which will be returned for all failure cases.
Having consistent error message structure for all APIs,
help the API consumers to write more robust code.
 */

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    // "10-01-2023 17:13:41"
    private final String dateTime;

    //General error message about nature of error
    private final String errorMessage;

    //Specific errors in API request processing
    private final List<ApiError> errors;

    public ErrorResponse(String dateTime, String errorMessage) {
        this.dateTime = dateTime;
        this.errorMessage = errorMessage;
        this.errors = null;
    }

    public ErrorResponse (String dateTime, String errorMessage, List<ApiError> errors) {
        this.dateTime = dateTime;
        this.errorMessage = errorMessage;
        this.errors = errors;
    }
}

package company.name.library.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

/*
Special class which will be returned for all failure cases.
Having consistent error message structure for all APIs,
help the API consumers to write more robust code.
 */
@RequiredArgsConstructor
@Getter
public class ErrorResponse {

    //General error message about nature of error
    private final String message;

    //Specific errors in API request processing
    private final List<String> details;

}
/*
{
  "dateTime": "10-01-2023 17:13:41",
  "message": "Request body contains invalid values",
  "errors": [
    {
      "fieldName": "name",
      "invalidValue": null,
      "errorMessage": "Book name can not be null"
    },
    {
      "fieldName": "author",
      "invalidValue": 123,
      "errorMessage": "Book author must contain letters and be longer than 5 characters"
    }
  ]
}
 */
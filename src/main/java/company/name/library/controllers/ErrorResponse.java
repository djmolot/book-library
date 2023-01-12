package company.name.library.controllers;
/*
Similarly, I have wrote a special class which will be returned for all failure cases.
Having consistent error message structure for all APIs,
help the API consumers to write more robust code.
 */
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {

    //General error message about nature of error
    private final String message;

    //Specific errors in API request processing
    private final List<String> details;

}
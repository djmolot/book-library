package company.name.library.controllers;

import company.name.library.entities.Book;
import company.name.library.entities.ErrorResponse;
import company.name.library.entities.Reader;
import company.name.library.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/readers")
@Validated
@Tag(name = "Reader Controller", description = "Endpoints for managing readers in the library")
public class ReaderController {
    private final LibraryService libraryService;

    @Operation(summary = "Get all readers registered in the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Readers was returned successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(example =
                                    "[{\"id\":1," +
                                            "\"name\":\"Zhirayr Hovik\"," +
                                            "\"books\":[]}," +
                                            "{\"id\":2," +
                                            "\"name\":\"Voski Daniel\"," +
                                            "\"books\":[]}]"))
            })
    })
    @GetMapping
    public ResponseEntity<List<Reader>> showAllReaders() {
        List<Reader> readers = libraryService.getAllReaders();
        return ResponseEntity.ok(readers);
    }

    @Operation(summary = "Add new reader to the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reader was added to the library",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reader.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid reader supplied",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"dateTime\": \"28.05.2023 19:27:24\"," +
                                    " \"errorMessage\": \"ArgumentValidation Error\"," +
                                    " \"errors\": [" +
                                    "   {" +
                                    "     \"fieldName\": \"name\"," +
                                    "     \"invalidValue\": \"Vo\"," +
                                    "     \"constraint\": \"Reader name must be between 3 and 50 characters\"" +
                                    "   }" +
                                    " ]}")) })
    })
    @PostMapping
    public ResponseEntity<Reader> registerNewReader(
            @Valid @RequestBody @Schema(description = "Reader object that needs to be added to the library",
                    example = "{\"name\": \"Voski Daniel\"}") Reader reader) {
        Reader readerFromDB = libraryService.registerNewReader(reader);
        return ResponseEntity.status(HttpStatus.CREATED).body(readerFromDB);
    }

    @Operation(summary = "Get all books borrowed by reader with {id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found books of the reader",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(example =
                                    "[{\"id\":1," +
                                            "\"title\":\"Java. The Complete Reference. Twelfth Edition\"," +
                                            "\"author\":\"Herbert Schildt\"," +
                                            "\"reader\":null}," +
                                            "{\"id\":2,\"title\":\"Java. An Introduction to Problem Solving & Programming\"," +
                                            "\"author\":\"Walter Savitch\"," +
                                            "\"reader\":null}]"))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid parameter supplied to method",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"dateTime\": \"28.05.2023 19:46:14\"," +
                                    " \"errorMessage\": \"Service layer Error. Reader with ID 555 does not exist in DB.\"}")) })
    })
    @GetMapping("/{readerId}/books")
    public ResponseEntity<List<Book>> showAllBooksBorrowedByReader(
            @Parameter(description = "id of reader to find his books")
            @PathVariable("readerId") @Positive Long readerId) {
        List<Book> books = libraryService.getAllBooksOfReader(readerId);
        return ResponseEntity.ok(books);
    }

}

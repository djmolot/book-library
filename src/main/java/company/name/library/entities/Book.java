package company.name.library.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.util.Optional;

import static company.name.library.controllers.ApiDocExamples.READER_FOUND;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Book {

    @Schema(description = "Long id", example = "1")
    private Long id;

    @Schema(description = "String author", example = "Herbert Schildt")
    @NotNull(message = "Please provide an author")
    @Size(min = 3, max = 50, message
            = "Author must be between 3 and 50 characters")
    private String author;

    @Schema(description = "String title", example = "Java. The Complete Reference. Twelfth Edition")
    @NotNull(message = "Please provide a title")
    @Size(min = 10, max = 255, message
            = "Title must be between 10 and 255 characters")
    private String title;

    @Schema(description = "Reader reader", example = READER_FOUND)
    private Optional<Reader> reader;

    @Schema(description = "Date borrow date", example = "2023-07-05")
    private Optional<LocalDate> borrowDate;

    @Schema(description = "Maximum borrow time in days", example = "14")
    @Min(value = 14, message = "maxBorrowTimeInDays must be at least 14 days")
    @Max(value = 30, message = "maxBorrowTimeInDays cannot exceed 30 days")
    private int maxBorrowTimeInDays;

    @Schema(description = "Determines whether the book is restricted for reading", example = "false")
    @NotNull(message = "Please provide a value for field restricted")
    private Boolean restricted;
}

package company.name.library.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reader {
    @Schema(description = "Long id", example = "1")
    private Long id;

    @NotNull(message = "Please provide a name of reader")
    @Size(min = 3, max = 50, message
            = "Reader name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Please provide a birthDate of reader")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    //@OneToMany
    private List<Book> books;

}

package company.name.library.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiError {

    @Schema(description = "Name of the field", example = "title")
    private final String fieldName;

    @Schema(description = "Value that contains error", example = "Java.")
    private final Object invalidValue;

    @Schema(description = "Сonstraint that imposed to field", example = "Title must be between 10 and 255 characters")
    private final String constraint;
}

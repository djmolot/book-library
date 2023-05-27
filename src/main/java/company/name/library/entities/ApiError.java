package company.name.library.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiError {

    private final String fieldName;

    private final Object invalidValue;

    private final String constraint;
}

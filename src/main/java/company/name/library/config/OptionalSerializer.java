package company.name.library.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Optional;

public class OptionalSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        if (value instanceof Optional<?> optional) {
            generator.writeObject(optional.orElse(null));
        }
    }
}

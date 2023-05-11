package company.name.library.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Book library API").version("1.0.0").description(
                        "This is a Spring Boot RESTful service \"book-library\"" +
                                " that uses springdoc-openapi and OpenAPI 3."));
    }
}

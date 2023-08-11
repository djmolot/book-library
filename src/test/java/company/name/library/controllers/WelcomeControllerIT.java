package company.name.library.controllers;

import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@AutoConfigureMockMvc
public class WelcomeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${library.maxNumberOfBooksToBorrow}")
    private int maxNumberOfBooksToBorrow;
    @Value("${library.minAgeOfReaderForRestricted}")
    private int minAgeOfReaderForRestricted;

    @Test
    public void testSayWelcome() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/welcome"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Welcome to the library!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentDate", is(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentTime", is(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.maxNumberOfBooksToBorrow", is(maxNumberOfBooksToBorrow)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minAgeOfReaderForRestricted", is(minAgeOfReaderForRestricted)));
    }
}

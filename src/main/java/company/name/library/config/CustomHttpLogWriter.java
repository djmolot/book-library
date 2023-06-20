package company.name.library.config;

import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Precorrelation;

import java.io.IOException;

public class CustomHttpLogWriter implements HttpLogWriter {
    @Override
    public void write(Precorrelation precorrelation, String request) throws IOException {
        System.out.println("Request Body: " + request);
    }

    @Override
    public void write(Correlation correlation, String response) throws IOException {
        System.out.println("Response Body: " + response);
    }
}

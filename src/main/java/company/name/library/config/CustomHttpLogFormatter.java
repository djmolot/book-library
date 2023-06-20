package company.name.library.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.zalando.logbook.*;
import com.fasterxml.jackson.databind.ObjectWriter;import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

final class CustomHttpLogFormatter implements HttpLogFormatter {
    private final ObjectWriter objectWriter;

    CustomHttpLogFormatter() {
        this.objectWriter = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .writerWithDefaultPrettyPrinter();
    }

    @Override
    public String format(Precorrelation precorrelation, HttpRequest request) throws IOException {
        final Map<String, Object> content = new LinkedHashMap<>();
        content.put("RequestMethod", request.getMethod());
        content.put("endpoint URL", request.getRequestUri());
        content.put("parameters", extractParametersFromQuery(request.getQuery()));
        content.put("headers", extractHeaders(request));
        content.put("body", request.getBodyAsString());

        String logLevel = "INFO";
        String formattedLogLevel = "\033[34m" + logLevel + "\033[0m";
        String formattedThread = "\033[35m" + Thread.currentThread().getName() + "\033[0m";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String className = stackTraceElements[3].getClassName();
        int lineNumber = stackTraceElements[3].getLineNumber();
        String formattedClassAndLine = "\033[33m" + className + ":" + lineNumber + "\033[0m";

        return getFormattedDate() + " " + formattedLogLevel + "  " + "[" + formattedThread + "] "
                + formattedClassAndLine + System.lineSeparator() + serializeContent(content);
    }

    @Override
    public String format(Correlation correlation, HttpResponse response) throws IOException {
        final Map<String, Object> content = new LinkedHashMap<>();
        content.put("status", response.getStatus());
        content.put("headers", extractHeaders(response));
        content.put("body", response.getBodyAsString());

        String logLevel = "INFO";
        String formattedLogLevel = "\033[34m" + logLevel + "\033[0m";
        String formattedThread = "\033[35m" + Thread.currentThread().getName() + "\033[0m";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String className = stackTraceElements[3].getClassName();
        int lineNumber = stackTraceElements[3].getLineNumber();
        String formattedClassAndLine = "\033[33m" + className + ":" + lineNumber + "\033[0m";

        return getFormattedDate() + " " + formattedLogLevel + "  " + "[" + formattedThread + "] "
                + formattedClassAndLine + System.lineSeparator() + serializeContent(content);
    }

    private String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    private Map<String, String> extractParametersFromQuery(String query) {
        Map<String, String> parameters = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] parameterPairs = query.split("&");
            for (String parameterPair : parameterPairs) {
                String[] keyValue = parameterPair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    parameters.put(key, value);
                }
            }
        }
        return parameters;
    }

    private Map<String, String> extractHeaders(HttpMessage message) {
        Map<String, String> extractedHeaders = new HashMap<>();
        message.getHeaders().forEach((name, values) -> {
            if (!values.isEmpty()) {
                extractedHeaders.put(name, values.get(0));
            }
        });
        return extractedHeaders;
    }

    private String serializeContent(Map<String, Object> content) throws JsonProcessingException {
        return objectWriter.writeValueAsString(content);
    }
}

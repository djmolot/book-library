package company.name.library.config;

import org.zalando.logbook.*;

import java.io.IOException;

public class CustomStrategy implements Strategy {

    @Override
    public HttpRequest process(final HttpRequest request) throws IOException {
        return request.withBody();
    }

    @Override
    public void write(final Precorrelation precorrelation, final HttpRequest request, final Sink sink)
            throws IOException {
        sink.write(precorrelation, request);
    }

    @Override
    public HttpResponse process(final HttpRequest request, final HttpResponse response) throws IOException {
        return response.withBody();
    }

    @Override
    public void write(final Correlation correlation, final HttpRequest request, final HttpResponse response,
                      final Sink sink) throws IOException {
        sink.write(correlation, request, response);
    }
}


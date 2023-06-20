package company.name.library.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.Strategy;
import org.zalando.logbook.core.DefaultHttpLogFormatter;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.core.DefaultStrategy;
import org.zalando.logbook.core.ExtendedLogFormatSink;
import org.zalando.logbook.core.SplunkHttpLogFormatter;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

/*
@Configuration
public class LogbookConfiguration {
    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .strategy(new CustomStrategy())
                .sink(new ExtendedLogFormatSink(
                        new DefaultHttpLogWriter(),
                        "c-ip s-dns cs-method cs-uri-stem cs-body sc-status sc-body time-taken cs(User-Agent) cs(Cookie) cs(Referrer) sc(Custom-Response-Header)"
                ))
                .build();
    }
}
*/
@Configuration
public class LogbookConfiguration {
    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .strategy(new CustomStrategy())
                .sink(new DefaultSink(
                        new SplunkHttpLogFormatter(),//DefaultHttpLogFormatter_JsonHttpLogFormatter
                        new DefaultHttpLogWriter()
                ))
                .build();
    }

}



package company.name.library.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomInfoContributor implements InfoContributor {
    @Value("${library.maxNumberOfBooksToBorrow}")
    private int maxNumberOfBooksToBorrow;
    @Value("${library.minAgeOfReaderForRestricted}")
    private int minAgeOfReaderForRestricted;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("maxNumberOfBooksToBorrow", maxNumberOfBooksToBorrow);
        builder.withDetail("minAgeOfReaderForRestricted", minAgeOfReaderForRestricted);
    }
}

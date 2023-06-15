package hexlet.code.config.rollbar;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;


@Configuration()
@ComponentScan({
        "hexlet.code",
})
public class RollbarConfig {

    @Value("${ROLLBAR_TOKEN:}")
    private String rollbarToken;
    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Bean
    public Rollbar rollbar() {

        return new Rollbar(getRollbarConfigs(rollbarToken));
    }

    private Config getRollbarConfigs(String accessToken) {

        return RollbarSpringConfigBuilder.withAccessToken(accessToken)
                .environment("dev")
                .enabled(Objects.equals(activeProfile, "prod"))
                .build();
    }
}

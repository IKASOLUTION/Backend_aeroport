package aeroport.bf.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class ProfileConfig {

    @Configuration
    @Profile("dev")
    @PropertySource({"classpath:config/application.yml", "classpath:config/application-dev.yml"})
    static class Dev {
    }

    @Configuration
    @Profile("prod")
    @PropertySource({"classpath:config/application.yml", "classpath:config/application-prod.yml"})
    static class Prod {
    }
}

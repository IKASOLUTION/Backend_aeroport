package aeroport.bf.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Open API Configuration class.
 */
@Configuration
@PropertySource("classpath:swagger/swagger-messages.properties")
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Aeroport APP API")
                        .description("RestFull APIs documentation.")
                        .version("1.0.0")
                        .termsOfService("https://www.aeroport.io/"));
    }
}

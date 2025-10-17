package aeroport.bf.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("aeroport.bf.repository")
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PersistenceConfig {


    /**
     * Create auditor.
     *
     * @return an instance of auditor impl
     */
    @Bean
    AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

    
}

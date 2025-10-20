package aeroport.bf.config;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import aeroport.bf.config.security.SecurityUtils;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * Get current auditor.
     *
     * @return username
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof String) {
            return Optional.of((String) authentication.getPrincipal());
        }
        return Optional.of(((User) authentication.getPrincipal()).getUsername());
    }


}

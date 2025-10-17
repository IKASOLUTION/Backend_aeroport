package aeroport.bf.config.security;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import aeroport.bf.repository.UserRepository;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TestUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public TestUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.FRENCH);
        Optional<aeroport.bf.domain.User> userFromDatabase = userRepository.findOneWithAuthoritiesByUsername(lowercaseLogin);

        return userFromDatabase.map(user -> new User(lowercaseLogin,
                user.getPassword(),
                getGrantedAuthorities(user))).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
                "database"));
    }

    private List<GrantedAuthority> getGrantedAuthorities(aeroport.bf.domain.User user) {
        return user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
    }
}

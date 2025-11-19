package aeroport.bf.service.util;

import aeroport.bf.config.security.SecurityUtils;
import aeroport.bf.domain.Aeroport;
import aeroport.bf.domain.User;
import aeroport.bf.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class CurrentUserAeropert {

    private static UserRepository userRepository;

    public CurrentUserAeropert(UserRepository userRepository) {
        CurrentUserAeropert.userRepository = userRepository;
    }

    public static Aeroport retrieveAeropert() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByUsername)
            .map(User::getAeroport)
            .orElse(null);
    }
}

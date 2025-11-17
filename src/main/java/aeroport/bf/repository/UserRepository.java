package aeroport.bf.repository;

import aeroport.bf.domain.User;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AbstractRepository<User, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByUsername";

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByUsername(String login);
    Optional<User> findByDeletedFalseAndUsername(String username);
    Optional<User> findByDeletedIsFalseAndUsername(String username);
    List<User> findByUsername(String username);
    User findTop1ByDeletedFalseAndUsername(String username);
    Optional<User> findOneByDeletedFalseAndUsername(String login);
    Optional<User> findOneWithAuthoritiesByEmail(String email);
    Optional<User> findByEmailAndDeletedFalse(String email);

    boolean existsByDeletedFalseAndEmailOrUsername(String email, String username);
    boolean existsByDeletedFalseAndUsername(String username);

    boolean existsByDeletedFalseAndEmailOrUsernameAndIdNot(String email, String username, Long id);
    Optional<User> findByVerificationToken(String token);
    List<User> findByEmailIsNotNullAndEmailNot(String email);
}

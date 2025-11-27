package aeroport.bf.repository;

import aeroport.bf.domain.Profil;

import java.util.List;
import java.util.Optional;

public interface ProfilRepository extends AbstractRepository<Profil, Long> {
    boolean existsByDeletedFalseAndCode(String code);
    boolean existsByDeletedFalseAndCodeAndIdNot(String code, Long id);
    List<Profil> findAllByDeletedFalse();
    Optional<Profil> findByDeletedFalseAndCode(String code);
}

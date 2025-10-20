package aeroport.bf.repository;

import aeroport.bf.domain.Profil;

import java.util.List;

public interface ProfilRepository extends AbstractRepository<Profil, Long> {
    boolean existsByDeletedFalseAndCode(String code);
    boolean existsByDeletedFalseAndCodeAndIdNot(String code, Long id);
    List<Profil> findAllByDeletedFalse();
}

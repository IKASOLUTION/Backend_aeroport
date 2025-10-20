package aeroport.bf.repository;
import aeroport.bf.domain.Exercice;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository

public interface ExerciceRepository extends AbstractRepository<Exercice, Long> {
    Optional<Exercice> findByAnnee(Long annee);
    boolean existsByDeletedFalseAndAnnee(Long annee);
    boolean existsByDeletedFalseAndAnneeAndIdNot(Long annee, Long id);
    List<Exercice> findAllByDeletedFalse();
    List<Exercice> findAllByDeletedFalseAndAnnee(Long annee);
    Optional<Exercice> findByActiveTrue();

}

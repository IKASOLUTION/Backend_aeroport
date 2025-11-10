package aeroport.bf.repository;

import aeroport.bf.domain.Ville;

import java.util.List;
import java.util.Optional;



public interface VilleRepository extends AbstractRepository<Ville, Long> {
    boolean existsByDeletedFalseAndNom(String nom);
    boolean existsByDeletedFalseAndNomAndIdNot(String nom, Long id);
     boolean existsByDeletedFalseAndNomIgnoreCase(String nom);
    List<Ville> findAllByDeletedFalse();
    Optional<Ville> findByNomIgnoreCaseAndDeletedFalse(String nom);

    
   

}

package aeroport.bf.repository;

import aeroport.bf.domain.Enregistrement;
import aeroport.bf.domain.Vol;
import aeroport.bf.domain.enums.StatutVol;
import aeroport.bf.domain.enums.StatutVoyageur;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnregistrementRepository extends AbstractRepository<Enregistrement, Long> {
    
    List<Enregistrement> findAllByDeletedFalse();
    
    @EntityGraph(attributePaths = {"voyage", "voyage.vol", "voyage.vol.aeroport"})
   @Query("SELECT e FROM Enregistrement e " +
       "WHERE e.deleted = false " +
       "AND e.voyage.vol.dateDepart BETWEEN :startDate AND :endDate " +
      // "AND (:aeroportId IS NULL OR e.voyage.vol.aeroport.id = :aeroportId) " +
       "AND (:statuts IS NULL OR e.statut IN :statuts)")
Page<Enregistrement> findByFilters(
    @Param("startDate") LocalDateTime startDate,
    @Param("endDate") LocalDateTime endDate,
    @Param("aeroportId") Long aeroportId,
    @Param("statuts") List<StatutVoyageur> statuts,
    Pageable pageable
);
 
}

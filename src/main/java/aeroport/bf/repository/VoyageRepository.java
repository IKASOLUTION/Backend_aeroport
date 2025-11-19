package aeroport.bf.repository;

import aeroport.bf.domain.Voyage;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoyageRepository extends AbstractRepository<Voyage, Long> {
    
    List<Voyage> findAllByDeletedFalse();
    
    /*Page<Voyage> findByDeletedFalseAndDateVoyageBetween(
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable
   );*/
    @Query("select v from Voyage v where v.deleted = false " +
            "AND ((CAST(:startDate AS date) IS NULL AND CAST(:endDate AS date) IS NOT NULL AND v.dateVoyage <= :endDate)" +
            "OR (CAST(:startDate AS date) IS NOT NULL AND CAST(:endDate AS date) IS NOT NULL AND v.dateVoyage BETWEEN :startDate AND :endDate)" +
            "OR (CAST(:startDate AS date) IS NOT NULL AND CAST(:endDate AS date) IS NULL AND v.dateVoyage >=:startDate)" +
            "OR (CAST(:startDate AS date) IS NULL AND CAST(:endDate AS date) IS NULL))" +
            "AND (:aeroport IS NULL OR v.aeroport.id =:aeroport)")
    Page<Voyage> findByDeletedFalseAndDateVoyageBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("aeroport") Long aeroport,
            Pageable pageable);

}

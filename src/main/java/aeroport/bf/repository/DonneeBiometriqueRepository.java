package aeroport.bf.repository;

import aeroport.bf.domain.DonneeBiometrique;
import aeroport.bf.domain.Enregistrement;
import aeroport.bf.domain.enums.StatutVoyageur;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;



public interface DonneeBiometriqueRepository extends AbstractRepository<DonneeBiometrique, Long> {

    @Query("select d from DonneeBiometrique d where d.deleted = false and :aeroport is null or d.aeroport.id=:aeroport")
    List<DonneeBiometrique> findAllByDeletedFalse(@Param("aeroport") Long aeroport);
    
    @Query("SELECT e FROM DonneeBiometrique e " +
       "WHERE e.deleted = false " +
       "AND e.dateCapture BETWEEN :startDate AND :endDate " +
       "AND (:aeroportId = 0L OR e.aeroport.id = :aeroportId) " 
      )
Page<DonneeBiometrique> findByFilters(
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate,
    @Param("aeroportId") Long aeroportId,
    Pageable pageable
);
 
   

}

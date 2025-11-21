package aeroport.bf.repository;

import aeroport.bf.domain.Vol;
import aeroport.bf.domain.Voyage;
import aeroport.bf.domain.enums.StatutVol;
import aeroport.bf.domain.enums.StatutVoyage;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
   



 @Query("SELECT v FROM Voyage v " +
           "WHERE v.deleted = false " +
           "AND (:statuts IS NULL OR v.statut IN :statuts) " +
           "AND v.dateVoyage >= :startDate " +
           "AND v.dateVoyage <= :endDate " +
           "AND (:aeroport IS NULL OR v.aeroportForUser.id = :aeroport)")
    Page<Voyage> findByDeletedFalseAndStatutInAndDateDepartBetween(
      @Param("statuts") List<StatutVoyage> statuts,
      @Param("startDate") LocalDate  startDate,
      @Param("endDate") LocalDate  endDate,
      @Param("aeroport") Long aeroport,
      Pageable pageable);




}

package aeroport.bf.repository;

import aeroport.bf.domain.Vol;
import aeroport.bf.domain.enums.StatutVol;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VolRepository extends AbstractRepository<Vol, Long> {
    boolean existsByDeletedFalseAndNumero(String numero);
    boolean existsByDeletedFalseAndNumeroAndIdNot(String numero, Long id);
    List<Vol> findAllByDeletedFalse();
    @Query("SELECT COUNT(p) FROM Vol p WHERE p.dateSaisie = :dateSaisie AND :aereport is null OR p.aeroport.id=:aereport")
    int countByDateSaisie(@Param("dateSaisie") LocalDate dateSaisie, @Param("aereport") Long aereport);

    @Query("select v from Vol v where v.deleted = false " +
            "AND (:statuts IS NULL OR v.statut IN :statuts) " +
            "AND ((CAST(:startDate AS date) IS NULL AND CAST(:endDate AS date) IS NOT NULL AND v.dateDepart <= :endDate)" +
            "OR (CAST(:startDate AS date) IS NOT NULL AND CAST(:endDate AS date) IS NOT NULL AND v.dateDepart BETWEEN :startDate AND :endDate)" +
            "OR (CAST(:startDate AS date) IS NOT NULL AND CAST(:endDate AS date) IS NULL AND v.dateDepart >=:startDate)" +
            "OR (CAST(:startDate AS date) IS NULL AND CAST(:endDate AS date) IS NULL))" +
            "AND (:aeroport IS NULL OR v.aeroport.id =:aeroport)")
    Page<Vol> findByDeletedFalseAndStatutInAndDateDepartBetween(
      @Param("statuts") List<StatutVol> statuts,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      @Param("aeroport") Long aeroport,
      Pageable pageable);

}

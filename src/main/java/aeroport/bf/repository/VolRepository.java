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
    @Query("SELECT COUNT(p) FROM Vol p WHERE p.dateSaisie = :dateSaisie")
    int countByDateSaisie(@Param("dateSaisie") LocalDate dateSaisie);
    
    Page<Vol> findByDeletedFalseAndStatutInAndDateDepartBetween(
      List<StatutVol> statuts,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Pageable pageable
   );

}

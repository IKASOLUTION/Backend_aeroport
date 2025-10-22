package aeroport.bf.repository;

import aeroport.bf.domain.Enregistrement;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnregistrementRepository extends AbstractRepository<Enregistrement, Long> {
    
    List<Enregistrement> findAllByDeletedFalse();
    
    Page<Enregistrement> findByDeletedFalseAndDateSaisieBetween(
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable
   );

}

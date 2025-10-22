package aeroport.bf.repository;

import aeroport.bf.domain.Voyage;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoyageRepository extends AbstractRepository<Voyage, Long> {
    
    List<Voyage> findAllByDeletedFalse();
    
    Page<Voyage> findByDeletedFalseAndDateVoyageBetween(
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable
   );

}

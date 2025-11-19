package aeroport.bf.repository;

import aeroport.bf.domain.InformationPersonnelle;

import java.lang.StackWalker.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InformationPersonnelleRepository extends AbstractRepository<InformationPersonnelle, Long> {
    
    List<InformationPersonnelle> findAllByDeletedFalse();
<<<<<<< HEAD
   Optional<InformationPersonnelle> findByNumeroNipAndDeletedFalse(String numeroNip);
    Page<InformationPersonnelle> findByDeletedFalseAndDateSaisieBetween(
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable
   );
=======
>>>>>>> c376742766109fbeda286363dc38c12ee81cef7e

}

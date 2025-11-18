package aeroport.bf.repository;

import aeroport.bf.domain.InformationPersonnelle;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InformationPersonnelleRepository extends AbstractRepository<InformationPersonnelle, Long> {
    
    List<InformationPersonnelle> findAllByDeletedFalse();

}

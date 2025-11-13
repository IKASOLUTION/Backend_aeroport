package aeroport.bf.repository;

import aeroport.bf.domain.MotifVoyage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface MotifVoyageRepository extends JpaRepository<MotifVoyage, Long> {
    List<MotifVoyage> findAllByDeletedIsFalse();

    Page<MotifVoyage> findAllByDeletedIsFalse(Pageable pageable);
}

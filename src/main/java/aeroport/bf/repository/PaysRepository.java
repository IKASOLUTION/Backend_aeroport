package aeroport.bf.repository;

import aeroport.bf.domain.Pays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface PaysRepository extends JpaRepository<Pays, Long> {
    List<Pays> findAllByDeletedIsFalse();

    Page<Pays> findAllByDeletedIsFalse(Pageable pageable);
}

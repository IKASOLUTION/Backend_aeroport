package aeroport.bf.repository;

import aeroport.bf.domain.Compagnie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface CompagnieRepository extends JpaRepository<Compagnie, Long> {
   
    List<Compagnie> findAllByDeletedIsFalse();
}

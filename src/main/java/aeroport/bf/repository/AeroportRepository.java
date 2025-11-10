package aeroport.bf.repository;

import aeroport.bf.domain.Aeroport;
import aeroport.bf.domain.Compagnie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface AeroportRepository extends JpaRepository<Aeroport, Long> {
    List<Aeroport> findAllByDeletedIsFalse();
}

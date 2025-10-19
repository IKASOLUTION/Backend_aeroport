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
    @Query("select c from Compagnie c where c.deleted =false " +
            "and (:pays  IS NULL OR c.pays.id =:pays)" +
            " and (:nom  IS NULL OR :nom='' OR c.nomCompagine LIKE CONCAT('%', CAST(:nom AS string), '%')) ")
    Page<Compagnie> findWithCriteria(Long pays , String nom, Pageable pageable);

    List<Compagnie> findAllByDeletedIsFalse();
}

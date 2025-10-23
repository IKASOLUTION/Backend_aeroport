package aeroport.bf.repository;

import aeroport.bf.domain.ListeNoire;
import aeroport.bf.domain.enums.Statut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface ListeNoireRepository extends JpaRepository<ListeNoire, Long> {

    @Query("select l from ListeNoire l where l.deleted =false " +
            " and (:nom  IS NULL OR :nom='' OR l.nom LIKE CONCAT('%', CAST(:nom AS string), '%')) " +
            "and (:prenom  IS NULL OR :prenom='' OR l.prenom LIKE CONCAT('%', CAST(:prenom AS string), '%'))" +
            "and (:lieuNaissance  IS NULL OR :lieuNaissance='' OR l.lieuNaissance LIKE CONCAT('%', CAST(:lieuNaissance AS string), '%'))" +
            "and (:statut  IS NULL OR :statut='' OR l.statut =:statut)" +
            "and (:dateNaissance  IS NULL  OR l.dateNaissance =:dateNaissance)" +"")
    Page<ListeNoire> findWithCriteria(
            String nom , String prenom, String lieuNaissance, Statut statut, LocalDate dateNaissance, Pageable pageable);

    List<ListeNoire> findAllByDeletedIsFalse();

}

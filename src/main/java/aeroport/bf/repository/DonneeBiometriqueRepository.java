package aeroport.bf.repository;

import aeroport.bf.domain.DonneeBiometrique;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface DonneeBiometriqueRepository extends AbstractRepository<DonneeBiometrique, Long> {

    @Query("select d from DonneeBiometrique d where d.deleted = false and :aeroport is null or d.aeroport.id=:aeroport")
    List<DonneeBiometrique> findAllByDeletedFalse(@Param("aeroport") Long aeroport);
    
   

}

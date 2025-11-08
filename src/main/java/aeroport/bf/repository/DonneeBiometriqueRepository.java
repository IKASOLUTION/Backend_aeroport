package aeroport.bf.repository;

import aeroport.bf.domain.DonneeBiometrique;

import java.util.List;



public interface DonneeBiometriqueRepository extends AbstractRepository<DonneeBiometrique, Long> {
    
    List<DonneeBiometrique> findAllByDeletedFalse();
    
   

}

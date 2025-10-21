package aeroport.bf.repository;

import aeroport.bf.domain.Vol;

import java.util.List;

public interface VolRepository extends AbstractRepository<Vol, Long> {
    boolean existsByDeletedFalseAndNumero(String numero);
    boolean existsByDeletedFalseAndNumeroAndIdNot(String numero, Long id);
    List<Vol> findAllByDeletedFalse();
}

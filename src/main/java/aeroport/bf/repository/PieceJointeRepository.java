package aeroport.bf.repository;


import java.util.List;

import aeroport.bf.domain.PieceJointe;

public interface PieceJointeRepository extends AbstractRepository<PieceJointe, Long> {
    boolean existsByDeletedFalseAndId(Long id);
    PieceJointe findOneByDeletedFalseAndId(Long id);

}

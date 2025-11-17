package aeroport.bf.dto.mapper;

import aeroport.bf.domain.MotifVoyage;
import aeroport.bf.dto.MotifVoyageDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for the entity Site and its DTO MotifVoyageDto.
 */

@Component
public class MotifVoyageMapper {
    public MotifVoyageDto toDto(MotifVoyage motifVoyage) {
        return MotifVoyageDto.builder()
                .id(motifVoyage.getId())
                .code(motifVoyage.getCode())
                .libelle(motifVoyage.getLibelle())
                .isDeleted(motifVoyage.getDeleted())
                .build();
    }

  public MotifVoyage toEntity(MotifVoyageDto motifVoyageDto) {
        return MotifVoyage.builder()
                .id(motifVoyageDto.getId())
                .code(motifVoyageDto.getCode())
                .libelle(motifVoyageDto.getLibelle())
                .deleted(motifVoyageDto.getIsDeleted())
                .build();
    }

    public List<MotifVoyageDto> toDtos(List<MotifVoyage> motifVoyages) {
        return motifVoyages.stream().map(this::toDto).toList();
    }

    public List<MotifVoyage> toEntities(List<MotifVoyageDto> motifVoyageDtos) {
        return motifVoyageDtos.stream().map(this::toEntity).toList();
    }
}

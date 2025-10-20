package aeroport.bf.dto.mapper;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aeroport.bf.domain.Exercice;
import aeroport.bf.dto.ExerciceDto;




/**
 * Mapper for the entity Site and its DTO SiteDTO.
 */
@Component
public class ExerciceMapper {

    public ExerciceDto toDto(Exercice dt) {
        return ExerciceDto.builder()
                .id(dt.getId())
                .annee(dt.getAnnee())
                .dateDebut(dt.getDateDebut())
                .dateFin(dt.getDateFin())
                .active(dt.getActive())
                .isDeleted(dt.getDeleted())
                .build();
    }

    public Exercice toEntity(ExerciceDto dto) {
        return Exercice.builder()
                .id(dto.getId())
                .annee(dto.getAnnee())
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .active(dto.getActive())
                .deleted(dto.getIsDeleted())
                .build();
    }

    public List<ExerciceDto> toDtos(List<Exercice> dts) {
        return dts.stream().map(this::toDto).toList();
    }

    public List<Exercice> toEntities(List<ExerciceDto> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
}

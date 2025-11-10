package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Ville;
import aeroport.bf.dto.VilleDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Mapper for the entity Site and its DTO PaysDto.
 */

@Component
public class VilleMapper {
   
    public VilleDto toDto(Ville ville) {
        return VilleDto.builder()
                .id(ville.getId())
                .nom(ville.getNom())
                .isDeleted(ville.getDeleted())
                .pays(ville.getPays())
                .build();
    }

    public Ville toEntity(VilleDto villeDto) {
        return Ville.builder()
                .id(villeDto.getId())
                .nom(villeDto.getNom())
                .pays(villeDto.getPays())
                .build();
    }

  

    public List<VilleDto> toDtos(List<Ville> villes) {
        return villes.stream().map(this::toDto).toList();
    }

    public List<Ville> toEntities(List<VilleDto> VilleDtos) {
        return VilleDtos.stream().map(this::toEntity).toList();
    }
}

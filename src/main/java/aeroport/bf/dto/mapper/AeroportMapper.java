package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Aeroport;
import aeroport.bf.domain.Pays;
import aeroport.bf.domain.Ville ;
import aeroport.bf.dto.AeroportDto;
import aeroport.bf.dto.PaysDto;
import aeroport.bf.dto.UserDto;
import aeroport.bf.dto.VilleDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for the entity Site and its DTO CompagnieDto.
 */

@Component
public class AeroportMapper {
    @Autowired
    PaysMapper paysMapper;
    VilleMapper villeMapper ;
    public AeroportDto toDto(Aeroport aeroport) {
        return AeroportDto.builder()
                .id(aeroport.getId())
                .nomAeroport(aeroport.getNomAeroport())
                .statutAeroport(aeroport.getStatutAeroport())
                .isDeleted(aeroport.getDeleted())
                .pays(aeroport.getPays())
                .ville(aeroport.getVille())
                .build();
    }

    public Aeroport toEntity(AeroportDto aeroportDto) {
        return Aeroport.builder()
                .id(aeroportDto.getId())
                .nomAeroport(aeroportDto.getNomAeroport())
                .statutAeroport(aeroportDto.getStatutAeroport())
                .pays(aeroportDto.getPays())
                .ville(aeroportDto.getVille())

                .build();
    }

    public AeroportDto toTrace(Aeroport aeroport) {
        return AeroportDto.builder()
                .id(aeroport.getId())
                .nomAeroport(aeroport.getNomAeroport())
                .statutAeroport(aeroport.getStatutAeroport())
                .isDeleted(aeroport.getDeleted())
                .pays(aeroport.getPays())
                .ville(aeroport.getVille())
                .build();
    }

    public List<AeroportDto> toDtos(List<Aeroport> aeroports) {
        return aeroports.stream().map(this::toDto).toList();
    }

    public List<Aeroport> toEntities(List<AeroportDto> aeroportDtos) {
        return aeroportDtos.stream().map(this::toEntity).toList();
    }
}

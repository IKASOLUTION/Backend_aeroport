package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Compagnie;
import aeroport.bf.domain.Pays;
import aeroport.bf.domain.Responsable;
import aeroport.bf.dto.CompagnieDto;
import aeroport.bf.dto.PaysDto;
import aeroport.bf.dto.ResponsableDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import java.util.List;


/**
 * Mapper for the entity Site and its DTO CompagnieDto.
 */




@Component
public class CompagnieMapper {

    public CompagnieDto toDto(Compagnie dt) {
        return CompagnieDto.builder()
                .id(dt.getId())
                .nomCompagine(dt.getNomCompagine())
                .groupe(dt.getGroupe())
                .adresseSiege(dt.getAdresseSiege())
                .contact(dt.getContact())
                .email(dt.getEmail())
                .mailResponsable(dt.getMailResponsable())
                .nationaliteResponsable(dt.getNationaliteResponsable())
                .nomResponsable(dt.getNomResponsable())
                .pays(dt.getPays())
                .prenomResponsable(dt.getPrenomResponsable())
                .siege(dt.getSiege())
                .statut(dt.getStatut())
                .telephoneResponsable(dt.getTelephoneResponsable())
                .isDeleted(dt.getDeleted())
                .build();
    }

    public Compagnie toEntity(CompagnieDto dto) {
        return Compagnie.builder()
                .id(dto.getId())
                .nomCompagine(dto.getNomCompagine())
                .groupe(dto.getGroupe())
                .adresseSiege(dto.getAdresseSiege())
                .contact(dto.getContact())
                .email(dto.getEmail())
                .mailResponsable(dto.getMailResponsable())
                .nationaliteResponsable(dto.getNationaliteResponsable())
                .nomResponsable(dto.getNomResponsable())
                .pays(dto.getPays())
                .prenomResponsable(dto.getPrenomResponsable())
                .siege(dto.getSiege())
                .statut(dto.getStatut())
                .telephoneResponsable(dto.getTelephoneResponsable())
                .build();
    }

    public List<CompagnieDto> toDtos(List<Compagnie> dts) {
        return dts.stream().map(this::toDto).toList();
    }

    public List<Compagnie> toEntities(List<CompagnieDto> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
}

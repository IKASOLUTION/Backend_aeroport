package aeroport.bf.dto.mapper;
import aeroport.bf.domain.Compagnie;
import aeroport.bf.domain.DonneeBiometrique;
import aeroport.bf.domain.ListeNoire;
import aeroport.bf.dto.CompagnieDto;
import aeroport.bf.dto.DonneeBiometriqueDto;
import aeroport.bf.dto.ListeNoireDto;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity Site and its DTO ListeNoireDTO.
 */

@Mapper(componentModel = "spring")
public class ListeNoireMapper  {
 
    public ListeNoireDto toDto(ListeNoire dt) {
        return ListeNoireDto.builder()
                .id(dt.getId())
                .lieuNaissance(dt.getLieuNaissance())
                .dateNaissance(dt.getDateNaissance())
                .motif(dt.getMotif())
                .nom(dt.getNom())
                .numeroCnib(dt.getNumeroCnib())
                .numeroNip(dt.getNumeroNip())
                .prenom(dt.getPrenom())
                .statut(dt.getStatut())
                .deleted(dt.getDeleted())
                .build();
    }

    public ListeNoire toEntity(ListeNoireDto dto) {
        return ListeNoire.builder()
                .id(dto.getId())
                .lieuNaissance(dto.getLieuNaissance())
                .dateNaissance(dto.getDateNaissance())
                .motif(dto.getMotif())
                .nom(dto.getNom())
                .numeroCnib(dto.getNumeroCnib())
                .numeroNip(dto.getNumeroNip())
                .prenom(dto.getPrenom())
                .statut(dto.getStatut())
                .build();
    }

    public List<ListeNoireDto> toDtos(List<ListeNoire> dts) {
        return dts.stream().map(this::toDto).toList();
    }

    public List<ListeNoire> toEntities(List<ListeNoireDto> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }

}

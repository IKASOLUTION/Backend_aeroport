package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Aeroport;
import aeroport.bf.domain.DonneeBiometrique;
import aeroport.bf.domain.InformationPersonnelle;
import aeroport.bf.dto.AeroportDto;
import aeroport.bf.dto.DonneeBiometriqueDto;
import aeroport.bf.dto.InformationPersonnelleDto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity Site and its DTO InformationPersonnelleDto.
 */

@Mapper(componentModel = "spring")
public interface InformationPersonnelleMapper extends EntityMapper<InformationPersonnelleDto, InformationPersonnelle> {

    @Mapping(target = "aeroport", source = "aeroport", qualifiedByName = "aeroportId")
    InformationPersonnelleDto toDto(InformationPersonnelle entity);

    @Named("aeroportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AeroportDto toDtoAeroportId(Aeroport entity);
}

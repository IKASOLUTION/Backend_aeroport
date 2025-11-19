package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Aeroport;
import aeroport.bf.domain.InformationPersonnelle;
import aeroport.bf.domain.Voyage;
import aeroport.bf.dto.AeroportDto;
import aeroport.bf.dto.InformationPersonnelleDto;
import aeroport.bf.dto.VoyageDto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity Site and its DTO PaysDto.
 */

@Mapper(componentModel = "spring", uses = {VilleMapper.class})
public interface VoyageMapper extends EntityMapper<VoyageDto, Voyage> {

    @Mapping(target = "aeroport", source = "aeroport", qualifiedByName = "aeroportId")
    VoyageDto toDto(Voyage entity);

    @Named("aeroportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AeroportDto toDtoAeroportId(Aeroport entity);
}

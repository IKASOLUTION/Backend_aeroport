package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Enregistrement;
import aeroport.bf.dto.EnregistrementDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Site and its DTO PaysDto.
 */

@Mapper(componentModel = "spring", uses = {VoyageMapper.class, InformationPersonnelleMapper.class})
public interface EnregistrementMapper extends EntityMapper<EnregistrementDto, Enregistrement> {
/*     @Mapping(target = "utilisateur", expression = "java(userMapper.toDto(entity.getUtilisateur()))")
 */    EnregistrementDto toDto(Enregistrement entity);
}

package aeroport.bf.dto.mapper;

import aeroport.bf.domain.InformationPersonnelle;
import aeroport.bf.dto.InformationPersonnelleDto;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity Site and its DTO InformationPersonnelleDto.
 */

@Mapper(componentModel = "spring")
public interface InformationPersonnelleMapper extends EntityMapper<InformationPersonnelleDto, InformationPersonnelle> {}

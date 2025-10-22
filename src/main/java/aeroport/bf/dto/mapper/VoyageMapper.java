package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Voyage;
import aeroport.bf.dto.VoyageDto;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity Site and its DTO PaysDto.
 */

@Mapper(componentModel = "spring", uses = {VilleMapper.class})
public interface VoyageMapper extends EntityMapper<VoyageDto, Voyage> {}

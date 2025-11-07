package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Ville;
import aeroport.bf.dto.VilleDto;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity Site and its DTO PaysDto.
 */

@Mapper(componentModel = "spring", uses = {PaysMapper.class})
public interface VilleMapper extends EntityMapper<VilleDto, Ville> {}

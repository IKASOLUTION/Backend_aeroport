package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Vol;
import aeroport.bf.dto.VolDto;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity Site and its DTO PaysDto.
 */

@Mapper(componentModel = "spring")
public interface VolMapper extends EntityMapper<VolDto, Vol> {}

package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Pays;
import aeroport.bf.dto.PaysDto;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Site and its DTO PaysDto.
 */

@Mapper(componentModel = "spring")
public interface PaysMapper extends EntityMapper<PaysDto, Pays> {}

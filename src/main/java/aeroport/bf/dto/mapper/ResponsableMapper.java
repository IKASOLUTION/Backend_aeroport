package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Responsable;
import aeroport.bf.dto.ResponsableDto;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Site and its DTO ResponsableDto.
 */

@Mapper(componentModel = "spring")
public interface ResponsableMapper extends EntityMapper<ResponsableDto, Responsable> {}

package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Compagnie;
import aeroport.bf.domain.Pays;
import aeroport.bf.dto.CompagnieDto;
import aeroport.bf.dto.PaysDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity Site and its DTO CompagnieDto.
 */

@Mapper(componentModel = "spring")
public interface CompagnieMapper extends EntityMapper<CompagnieDto, Compagnie> {
    @Mapping(target = "pays", source = "pays", qualifiedByName = "paysId")
    CompagnieDto toDto(Compagnie s);

    @Named("paysId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "code", source = "code")
    PaysDto toDtoPaysId(Pays pays);
}

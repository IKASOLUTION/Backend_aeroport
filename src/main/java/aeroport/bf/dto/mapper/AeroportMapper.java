package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Aeroport;
import aeroport.bf.domain.Pays;
import aeroport.bf.dto.AeroportDto;
import aeroport.bf.dto.PaysDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity Site and its DTO CompagnieDto.
 */

@Mapper(componentModel = "spring")
public interface AeroportMapper extends EntityMapper<AeroportDto, Aeroport> {
    @Mapping(target = "pays", source = "pays", qualifiedByName = "paysId")
    @Mapping(target= "ville", source = "ville", qualifiedByName = "villeId")
    AeroportDto toDto(Aeroport s);

    @Named("paysId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "code", source = "code")
    PaysDto toDtoPaysId(Pays pays);

    @Named("villeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
   
    PaysDto toDtoVilleId(Ville ville);
}

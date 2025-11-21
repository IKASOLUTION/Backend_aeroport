package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Aeroport;
import aeroport.bf.domain.InformationPersonnelle;
import aeroport.bf.domain.Ville;
import aeroport.bf.domain.Voyage;
import aeroport.bf.dto.AeroportDto;
import aeroport.bf.dto.InformationPersonnelleDto;
import aeroport.bf.dto.VoyageDto;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import java.util.List;


/**
 * Mapper for the entity Site and its DTO PaysDto.
 */
@Mapper(componentModel = "spring", uses = {VilleMapper.class, AeroportMapper.class})
public interface VoyageMapper extends EntityMapper<VoyageDto, Voyage> {

    // --- ENTITY -> DTO ---
   
    @Mapping(source = "aeroport.id", target = "aeroportId")
    @Mapping(source = "aeroportForUser.id", target = "aeroportForUserId")
    @Mapping(source = "aeroportForUser.nomAeroport", target = "nomAgentConnecteAeroport")

 
    VoyageDto toDto(Voyage voyage);

    // --- DTO -> ENTITY ---
    // on demande d'utiliser les méthodes fromId pour construire les relations
    @Mapping(source = "aeroportId", target = "aeroport")
    
    Voyage toEntity(VoyageDto voyageDto);

    List<VoyageDto> toDto(List<Voyage> voyages);
    List<Voyage> toEntity(List<VoyageDto> voyagesDtos);

    // --- helper factories utilisés par MapStruct ---
   

    default Aeroport aeroportFromId(Long id) {
        if (id == null) return null;
        Aeroport a = new Aeroport();
        a.setId(id);
        return a;
    }

    default Ville villeFromId(Long id) {
        if (id == null) return null;
        Ville v = new Ville();
        v.setId(id);
        return v;
    }

  
    @org.mapstruct.Named("aeroportFromId")
    default Aeroport fromAeroportId(Long id) { return aeroportFromId(id); }

    @org.mapstruct.Named("villeFromId")
    default Ville fromVilleId(Long id) { return villeFromId(id); }

    // --- Optionnel : remplir des champs du DTO à partir des entités (ex: noms) ---
    @AfterMapping
    default void fillNames(Voyage voyage, @MappingTarget VoyageDto dto) {
        if (voyage == null) return;
       
       
        if (voyage.getAeroport() != null) {
            dto.setAeroportId(voyage.getAeroport().getId());
        }
    }
}



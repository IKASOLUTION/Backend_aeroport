package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Aeroport;
import aeroport.bf.domain.Compagnie;
import aeroport.bf.domain.Enregistrement;
import aeroport.bf.domain.Ville;
import aeroport.bf.domain.Vol;
import aeroport.bf.dto.VolDto;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper for the entity Site and its DTO PaysDto.
 */
@Mapper(componentModel = "spring", uses = {VilleMapper.class, CompagnieMapper.class, AeroportMapper.class})
public interface VolMapper extends EntityMapper<VolDto, Vol> {

    // --- ENTITY -> DTO ---
    @Mapping(source = "compagnie.id", target = "compagnieId")
    @Mapping(source = "aeroport.id", target = "aeroportId")
   // @Mapping(source = "villeDepart.id", target = "villeDepartId")
   // @Mapping(source = "villeArrivee.id", target = "villeArriveeId")
    // dateDepart/dateArrivee mappés automatiquement si mêmes noms
    VolDto toDto(Vol vol);

    // --- DTO -> ENTITY ---
    // on demande d'utiliser les méthodes fromId pour construire les relations
    @Mapping(source = "compagnieId", target = "compagnie")
    @Mapping(source = "aeroportId", target = "aeroport")
    //@Mapping(source = "villeDepartId", target = "villeDepart")
    //@Mapping(source = "villeArriveeId", target = "villeArrivee")
    Vol toEntity(VolDto volDto);

    List<VolDto> toDto(List<Vol> vols);
    List<Vol> toEntity(List<VolDto> volDtos);

    // --- helper factories utilisés par MapStruct ---
    default Compagnie compagnieFromId(Long id) {
        if (id == null) return null;
        Compagnie c = new Compagnie();
        c.setId(id);
        return c;
    }

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

    // MapStruct choisira automatiquement ces méthodes si on nomme correctement les cibles.
    // Mais pour s'assurer qu'il les utilise, on peut ajouter méthodes génériques :
    @org.mapstruct.Named("compagnieFromId")
    default Compagnie fromCompagnieId(Long id) { return compagnieFromId(id); }

    @org.mapstruct.Named("aeroportFromId")
    default Aeroport fromAeroportId(Long id) { return aeroportFromId(id); }

    @org.mapstruct.Named("villeFromId")
    default Ville fromVilleId(Long id) { return villeFromId(id); }

    // --- Optionnel : remplir des champs du DTO à partir des entités (ex: noms) ---
    @AfterMapping
    default void fillNames(Vol vol, @MappingTarget VolDto dto) {
        if (vol == null) return;
       /*  if (vol.getVilleDepart() != null) {
            dto.setVilleNomD(vol.getVilleDepart().getNom());
        }
        if (vol.getVilleArrivee() != null) {
            dto.setVilleNomA(vol.getVilleArrivee().getNom());
        } */
        if (vol.getCompagnie() != null) {
            dto.setCompagnieId(vol.getCompagnie().getId());
        }
        if (vol.getAeroport() != null) {
            dto.setAeroportId(vol.getAeroport().getId());
        }
    }
}



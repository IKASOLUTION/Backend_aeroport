package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Enregistrement;
import aeroport.bf.dto.EnregistrementDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Site and its DTO PaysDto.
 */

@Mapper(componentModel = "spring", uses = {VoyageMapper.class, InformationPersonnelleMapper.class})
public interface EnregistrementMapper extends EntityMapper<EnregistrementDto, Enregistrement> {
/**
     * Mapping complet de l'entité vers le DTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "statut", source = "statut")
    @Mapping(target = "exercice", source = "exercice")
    @Mapping(target = "dateSaisie", source = "dateSaisie")
    
    // Relations complètes (utilise les mappers associés)
    @Mapping(target = "voyage", source = "voyage")
    @Mapping(target = "informationPersonnelle", source = "informationPersonnel")
    
    // === Document (depuis InformationPersonnelle) ===
    @Mapping(target = "typeDocument", source = "informationPersonnel.typeDocument")
    @Mapping(target = "numeroDocument", source = "informationPersonnel.numeroDocument")
    @Mapping(target = "numeroNip", source = "informationPersonnel.numeroNip")
    @Mapping(target = "dateDelivrance", source = "informationPersonnel.dateDelivrance")
    @Mapping(target = "lieuDelivrance", source = "informationPersonnel.lieuDelivrance")
    
    // Pièces jointes (ignorées - gérées séparément via MultipartFile)
    @Mapping(target = "photoProfil", ignore = true)
    @Mapping(target = "imageRecto", ignore = true)
    @Mapping(target = "imageVerso", ignore = true)
    
    // === Informations personnelles (depuis InformationPersonnelle) ===
    @Mapping(target = "nomFamille", source = "informationPersonnel.nomFamille")
    @Mapping(target = "prenom", source = "informationPersonnel.prenom")
    @Mapping(target = "dateNaissance", source = "informationPersonnel.dateNaissance")
    @Mapping(target = "lieuNaissance", source = "informationPersonnel.lieuNaissance")
    @Mapping(target = "nationalite", source = "informationPersonnel.nationalite")
    @Mapping(target = "profession", source = "informationPersonnel.profession")
    
    // === Coordonnées (depuis InformationPersonnelle et Enregistrement) ===
    @Mapping(target = "paysResidence", source = "informationPersonnel.paysResidence")
    @Mapping(target = "emailContact", source = "informationPersonnel.emailContact")
    @Mapping(target = "telephoneBurkina", source = "informationPersonnel.telephoneBurkina")
    @Mapping(target = "adresseBurkina", source = "informationPersonnel.adresseBurkina")
    @Mapping(target = "telephoneEtranger", source = "telephoneEtranger")
    @Mapping(target = "adresseEtranger", source = "adresseEtranger")
    
    // === Voyage (depuis Voyage) ===
    @Mapping(target = "volId", source = "voyage.vol.id")
    @Mapping(target = "villeDepart", source = "voyage.villeDepart.nom")
    @Mapping(target = "villeDestination", source = "voyage.villeDestination.nom")
    @Mapping(target = "dateVoyage", source = "voyage.dateVoyage")
    @Mapping(target = "heureVoyage", source = "voyage.heureVoyage")
    @Mapping(target = "motifVoyage", source = "voyage.motifVoyage")
    @Mapping(target = "etatVoyage", source = "voyage.etatVoyage")
    @Mapping(target = "dureeSejour", source = "voyage.dureeSejour")

    // === Aeroport (depuis Aeroport) ===
    @Mapping(target = "aeroportId", source = "aeroport.id")
    
    EnregistrementDto toDto(Enregistrement entity);
}

package aeroport.bf.dto.mapper;

import aeroport.bf.domain.DonneeBiometrique;
import aeroport.bf.domain.Enregistrement;
import aeroport.bf.domain.InformationPersonnelle;
import aeroport.bf.domain.ListeNoire;
import aeroport.bf.dto.DonneeBiometriqueDto;
import aeroport.bf.dto.EnregistrementDto;
import aeroport.bf.dto.ListeNoireDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity Site and its DTO PaysDto.
 */

@Mapper(componentModel = "spring", uses = {EnregistrementMapper.class})
public interface DonneeBiometriqueMapper extends EntityMapper<DonneeBiometriqueDto, DonneeBiometrique> {
    
    // Entity -> DTO : Extraire l'ID de l'enregistrement
    @Mapping(target = "informationPersonnelleId", source = "informationPersonnelle.id")
    DonneeBiometriqueDto toDto(DonneeBiometrique entity);

    // DTO -> Entity : Créer l'enregistrement avec seulement l'ID
    
    @Mapping(target = "informationPersonnelle", source = "informationPersonnelleId", qualifiedByName = "idToInformationPersonnelle")
    DonneeBiometrique toEntity(DonneeBiometriqueDto dto);

    // Méthode helper pour créer un Enregistrement avec seulement l'ID
    @Named("idToInformationPersonnelle")
    default InformationPersonnelle idToInformationPersonnelle(Long informationPersonnelleId) {
        if (informationPersonnelleId == null) {
            return null;
        }
        InformationPersonnelle informationPersonnelle = new InformationPersonnelle();
        informationPersonnelle.setId(informationPersonnelleId);
        return informationPersonnelle;
    }
}

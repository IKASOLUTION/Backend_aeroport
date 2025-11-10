package aeroport.bf.dto.mapper;

import aeroport.bf.domain.DonneeBiometrique;
import aeroport.bf.domain.Enregistrement;
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
    @Mapping(target = "enregistrementId", source = "enregistrement.id")
    DonneeBiometriqueDto toDto(DonneeBiometrique entity);

    // DTO -> Entity : Créer l'enregistrement avec seulement l'ID
    @Mapping(target = "enregistrement", source = "enregistrementId", qualifiedByName = "idToEnregistrement")
    DonneeBiometrique toEntity(DonneeBiometriqueDto dto);

    // Méthode helper pour créer un Enregistrement avec seulement l'ID
    @Named("idToEnregistrement")
    default Enregistrement idToEnregistrement(Long enregistrementId) {
        if (enregistrementId == null) {
            return null;
        }
        Enregistrement enregistrement = new Enregistrement();
        enregistrement.setId(enregistrementId);
        return enregistrement;
    }
}

package aeroport.bf.dto.mapper;
import aeroport.bf.domain.DonneeBiometrique;
import aeroport.bf.domain.ListeNoire;
import aeroport.bf.dto.DonneeBiometriqueDto;
import aeroport.bf.dto.ListeNoireDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity Site and its DTO ListeNoireDTO.
 */

@Mapper(componentModel = "spring")
public interface ListeNoireMapper extends EntityMapper<ListeNoireDto, ListeNoire> {
    @Mapping(target = "donneeBiometrique", source = "donneeBiometrique", qualifiedByName = "donneeBiometriqueId")
    ListeNoireDto toDto(ListeNoire s);

    @Named("donneeBiometriqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DonneeBiometriqueDto toDtoDonneeBiometriqueIdId(DonneeBiometrique s);


}

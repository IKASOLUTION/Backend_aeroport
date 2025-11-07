package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Ville;
import aeroport.bf.dto.VilleDto;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity Site and its DTO PaysDto.
 */

@Mapper(componentModel = "spring", uses = {CompagnieMapper.class, AeroportMapper.class})
public interface VilleMapper extends EntityMapper<VilleDto, Ville> {}


@Component
public class VilleMapper {
    @Autowired
    PaysMapper paysMapper;
    public VilleDto toDto(Ville ville) {
        return VilleDto.builder()
                .id(ville.getId())
                .nom(ville.getNom())
                .isDeleted(ville.getDeleted())
                .pays(ville.getPays())
                .build();
    }

    public Ville toEntity(VilleDto villeDto) {
        return Ville.builder()
                .id(villeDto.getId())
                .nom(villeDto.getNom())
                .pays(userDto.getPays())
                .build();
    }

  

    public List<VilleDto> toDtos(List<Ville> villes) {
        return villes.stream().map(this::toDto).toList();
    }

    public List<Ville> toEntities(List<VilleDto> VilleDtos) {
        return VilleDtos.stream().map(this::toEntity).toList();
    }
}

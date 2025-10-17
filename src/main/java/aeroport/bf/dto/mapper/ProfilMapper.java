package aeroport.bf.dto.mapper;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aeroport.bf.domain.Profil;
import aeroport.bf.dto.ProfilDto;

/**
 * Mapper for the entity Site and its DTO SiteDTO.
 */
@Component
public class ProfilMapper {
    @Autowired
    MenuActionMapper mapper;
    public ProfilDto toDto(Profil dt) {
        return ProfilDto.builder()
                .id(dt.getId())
                .code(dt.getCode())
                .libelle(dt.getLibelle())
                .isDeleted(dt.getDeleted())
                .menus(dt.getMenus())
                
                .build();
    }

    public Profil toEntity(ProfilDto dto) {
        return Profil.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .libelle(dto.getLibelle())
                .menus(dto.getMenus())
                .deleted(dto.getIsDeleted())
                
                .build();
    }

    public List<ProfilDto> toDtos(List<Profil> dts) {
        return dts.stream().map(this::toDto).toList();
    }

    public List<Profil> toEntities(List<ProfilDto> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
}

package aeroport.bf.dto.mapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aeroport.bf.domain.MenuAction;
import aeroport.bf.domain.ModuleParam;
import aeroport.bf.dto.MenuActionDto;

/**
 * Mapper for the entity  MenuAction and its DTO.
 */
@Component
public class MenuActionMapper {
    @Autowired
    ModuleParamMapper moduleParamMapper;
    public MenuActionDto toDto(MenuAction menuAction) {
    return MenuActionDto.builder()
            .id(menuAction.getId())
            .menuActionLibelle(menuAction.getMenuActionLibelle())
            .menuActionCode(menuAction.getMenuActionCode())
            .isDeleted(menuAction.getDeleted())
            .moduleParamId(menuAction.getModuleParam().getId())
            .moduleParamLibelle(menuAction.getModuleParam().getModuleParamLibelle())
            .build();
}

public MenuAction toEntity(MenuActionDto menuActionDto) {
    return MenuAction.builder()
            .id(menuActionDto.getId())
            .menuActionLibelle(menuActionDto.getMenuActionLibelle())
            .menuActionCode(menuActionDto.getMenuActionCode())
            .deleted(menuActionDto.getIsDeleted())
            .moduleParam(ModuleParam.builder()
                        .id(menuActionDto.getModuleParamId())
                        .build())
            .build();
}

public Set<MenuActionDto> setToDtos(Set<MenuAction> menuActions) {
    return  menuActions.stream().map(this::toDto).collect(Collectors.toSet());
}
public Set<MenuAction> setToEntities(Set<MenuActionDto> menuActionDtos) {
    return  menuActionDtos.stream().map(this::toEntity).collect(Collectors.toSet());
}
public List<MenuActionDto> toDtos(List<MenuAction> menuActions) {
    return menuActions.stream().map(this::toDto).toList();
}

public List<MenuAction> toEntities(List<MenuActionDto> menuActionDtos) {
    return menuActionDtos.stream().map(this::toEntity).toList();
}

}

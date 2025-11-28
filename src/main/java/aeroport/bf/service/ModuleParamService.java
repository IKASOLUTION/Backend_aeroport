package aeroport.bf.service;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import aeroport.bf.config.audit.EntityAuditAction;

import aeroport.bf.domain.MenuAction;
import aeroport.bf.domain.ModuleParam;
import aeroport.bf.domain.Trace;
import aeroport.bf.dto.MenuActionDto;
import aeroport.bf.dto.ModuleParamDto;
import aeroport.bf.dto.mapper.MenuActionMapper;
import aeroport.bf.dto.mapper.ModuleParamMapper;
import aeroport.bf.repository.MenuActionRepository;
import aeroport.bf.repository.ModuleParamRepository;
import aeroport.bf.repository.TraceRepository;



@Service
@Transactional
@RequiredArgsConstructor
public class ModuleParamService {
    private final ModuleParamRepository moduleParamRepository;
    private final ModuleParamMapper moduleParamMapper;
    private final MenuActionRepository menuActionRepository;
    private final MenuActionMapper menuActionMapper;
    private final TraceRepository traceRepository;


/**
 * Save moduleParam.
 *
 * @param moduleParamDto {@link aeroport.bf.dto.ModuleParamDto}
 * @return saved moduleParam object
 */

private ModuleParamDto saveModuleParam(final ModuleParamDto moduleParamDto) {
    // 1. Save or update ModuleParam
    ModuleParam moduleParam = moduleParamMapper.toEntity(moduleParamDto);
    moduleParam.setDeleted(Boolean.FALSE);
    ModuleParam savedModuleParam = moduleParamRepository.save(moduleParam);

    // 2. Synchronize MenuActions
    synchronizeMenuActions(moduleParamDto.getMenuActions(), savedModuleParam);

    // 3. Return updated DTO
    return buildModuleParamDto(savedModuleParam);
}

private void synchronizeMenuActions(List<MenuActionDto> menuActionDtos, ModuleParam moduleParam) {
    // Get existing menu actions
    List<MenuAction> existingMenuActions = menuActionRepository
            .findByModuleParamIdAndDeletedFalse(moduleParam.getId());
    
    Set<Long> processedIds = new HashSet<>();

    // Process incoming menu actions
    if (menuActionDtos != null && !menuActionDtos.isEmpty()) {
        for (MenuActionDto dto : menuActionDtos) {
            Long savedId = processMenuAction(dto, moduleParam);
            if (savedId != null) {
                processedIds.add(savedId);
            }
        }
    }

    // Mark removed menu actions as deleted
    markRemovedMenuActionsAsDeleted(existingMenuActions, processedIds);
}

private Long processMenuAction(MenuActionDto dto, ModuleParam moduleParam) {
    if (dto.getId() != null && dto.getId() > 0) {
        // Update existing or create if not found
        return updateOrCreateMenuAction(dto, moduleParam);
    } else {
        // Create new menu action
        return createNewMenuAction(dto, moduleParam);
    }
}

private Long updateOrCreateMenuAction(MenuActionDto dto, ModuleParam moduleParam) {
    Optional<MenuAction> existingOpt = menuActionRepository
            .findTop1ByDeletedFalseAndId(dto.getId());
    
    if (existingOpt.isPresent()) {
        // Update existing
        MenuAction existing = existingOpt.get();
        validateMenuCodeUniqueness(dto, existing.getId());
        
        existing.setMenuActionLibelle(dto.getMenuActionLibelle());
        existing.setMenuActionCode(dto.getMenuActionCode());
        existing.setDeleted(Boolean.FALSE);
        existing.setModuleParam(moduleParam);
        
        MenuAction saved = menuActionRepository.save(existing);
        return saved.getId();
    } else {
        // Create new if not found (maybe was deleted)
        return createNewMenuAction(dto, moduleParam);
    }
}

private Long createNewMenuAction(MenuActionDto dto, ModuleParam moduleParam) {
    validateMenuCodeUniqueness(dto, null);
    
    MenuAction newAction = MenuAction.builder()
            .menuActionLibelle(dto.getMenuActionLibelle())
            .menuActionCode(dto.getMenuActionCode())
            .deleted(Boolean.FALSE)
            .moduleParam(moduleParam)
            .build();
    
    MenuAction saved = menuActionRepository.save(newAction);
    return saved.getId();
}

private void validateMenuCodeUniqueness(MenuActionDto dto, Long excludeId) {
    if (isExisteMenuByCode(dto, excludeId)) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT, 
                "Le code '" + dto.getMenuActionCode() + "' existe déjà."
        );
    }
}

private boolean isExisteMenuByCode(MenuActionDto dto, Long excludeId) {
    // You need to modify your existing isExisteMenuByCode method to accept excludeId
    // to exclude the current record when checking for duplicates during updates
    Optional<MenuAction> existing = menuActionRepository
            .findByMenuActionCodeAndDeletedFalse(dto.getMenuActionCode());
    
    if (existing.isEmpty()) {
        return false;
    }
    
    // If excludeId is provided, check if it's the same record
    return excludeId == null || !existing.get().getId().equals(excludeId);
}

private void markRemovedMenuActionsAsDeleted(List<MenuAction> existingMenuActions, 
                                              Set<Long> processedIds) {
    if (existingMenuActions != null && !existingMenuActions.isEmpty()) {
        existingMenuActions.stream()
                .filter(existing -> !processedIds.contains(existing.getId()))
                .forEach(existing -> {
                    existing.setDeleted(Boolean.TRUE);
                    menuActionRepository.save(existing);
                });
    }
}

private ModuleParamDto buildModuleParamDto(ModuleParam moduleParam) {
    ModuleParamDto dto = moduleParamMapper.toDto(moduleParam);
    
    // Load and attach menu actions
    List<MenuAction> menuActions = menuActionRepository
            .findByModuleParamIdAndDeletedFalse(moduleParam.getId());
    
    if (!menuActions.isEmpty()) {
        MenuActionMapper menuActionMapper = new MenuActionMapper();
        dto.setMenuActions(menuActionMapper.toDtos(menuActions));
    }
    
    return dto;
}
/**
 * Create new moduleParam.
 *
 * @param moduleParamDto {@link aeroport.bf.dto.ModuleParamDto}
 * @return created moduleParam object
 */
public ModuleParamDto createModuleParam(final ModuleParamDto moduleParamDto) {

    if (isExisteByCode(moduleParamDto)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce code existe déjà.");
    }



    return saveModuleParam(moduleParamDto);
}

/**
 * Update existing moduleParam.
 *
 * @param moduleParamDto {@link aeroport.bf.dto.ModuleParamDto}
 * @return updated moduleParam object
 */
public ModuleParamDto updateModuleParam(final ModuleParamDto moduleParamDto, final long id) {
    if (!moduleParamRepository.existsByDeletedFalseAndId(id)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No moduleParam exists with this ID : %d", id));
    }

    if (Objects.isNull(moduleParamDto.getId())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created moduleParam cannot have null ID.");
    }
    if (isExisteByCode(moduleParamDto)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce code existe déjà.");
    }

    return saveModuleParam(moduleParamDto);
}

/**
 * Get moduleParam by id.
 *
 * @param id searched moduleParam id
 * @return found moduleParam object
 */
public ModuleParamDto findModuleParam(final long id) {
    if (!moduleParamRepository.existsByDeletedFalseAndId(id)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No moduleParam exists with this ID : %d", id));
    }

    return moduleParamMapper.toDto(moduleParamRepository.findOneByDeletedFalseAndId(id));
}


public List<MenuActionDto> findMenuByModule(Long idModule) {
    return menuActionMapper.toDtos(menuActionRepository.findByModuleParamIdAndDeletedFalse(idModule));
}
/**
 * Fetch all moduleParam stored in DB.
 *
 * @return list of {@link aeroport.bf.dto.ModuleParamDto}
 */
public List<ModuleParamDto> findAllModuleParam() {
    List<ModuleParam> moduleParams = moduleParamRepository.findAllByDeletedFalse();
    if (moduleParams.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found, Please create at least one moduleParam before.");
    }
    return moduleParamMapper.toDtos(moduleParams);
}

/**
 * Remove a moduleParam by id if exists.
 *
 * @param id removed moduleParam id.
 */
public void deleteModuleParam(final long id) {
    moduleParamRepository.findTop1ByDeletedFalseAndId(id).ifPresentOrElse(moduleParam -> {
        moduleParam.setDeleted(Boolean.TRUE);
        moduleParamRepository.save(moduleParam);
      //  traceService.writeAuditEvent(moduleParam, EntityAuditAction.DELETE);
        
       
    },

    () -> {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove moduleParam with ID : %d", id));
    });
    menuActionRepository.findByModuleParamIdAndDeletedFalse(id).stream().peek(menu-> {
        menu.setDeleted(true);
        menuActionRepository.save(menu);
       // traceService.writeAuditEvent(menu, EntityAuditAction.DELETE);
    }).collect(Collectors.toList());
}

/**
 * Remove a moduleParam list
 *
 * @param dtos removed moduleParam.
 */
public List<ModuleParamDto> deleteAll(List<ModuleParamDto> dtos) {
    moduleParamMapper.toEntities(dtos).stream().peek(moduleParam-> {
        moduleParam.setDeleted(Boolean.TRUE);
        moduleParamRepository.save(moduleParam);
        Trace trace1 = new Trace();
        
        trace1.setAction(EntityAuditAction.DELETE.toString());
        traceRepository.save(trace1);
        menuActionRepository.findByModuleParamIdAndDeletedFalse(moduleParam.getId()).stream().peek(menu-> {
            menu.setDeleted(true);
            menuActionRepository.save(menu);
           
           
        }).collect(Collectors.toList());
    }).collect(Collectors.toList());
    return dtos;
}


     public Boolean isExisteByCode(final ModuleParamDto moduleParamDto) {
        Boolean isExiste = false;
            if(moduleParamDto.getId() !=null) {

                if(!moduleParamRepository.findByDeletedFalseAndModuleParamCodeIgnoreCase(moduleParamDto.getModuleParamCode()).isEmpty() 
                && moduleParamRepository.findByDeletedFalseAndModuleParamCodeIgnoreCase(moduleParamDto.getModuleParamCode()).size() > 1) {
                    isExiste = true;               
                    } 
                
            } else {

                isExiste = moduleParamRepository.existsByDeletedFalseAndModuleParamCodeIgnoreCase(moduleParamDto.getModuleParamCode());
    
                }

                if (isExiste.equals(Boolean.TRUE)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce code existe déjà");
                }
            return isExiste;
        }

    public Boolean isExisteMenuByCode(final MenuActionDto menuActionDto) {
        Boolean isExiste = false;
        if(menuActionDto.getId() !=null) {
            if(!menuActionRepository.findByDeletedFalseAndMenuActionCodeIgnoreCase(menuActionDto.getMenuActionCode()).isEmpty() 
            && menuActionRepository.findByDeletedFalseAndMenuActionCodeIgnoreCase(menuActionDto.getMenuActionCode()).size() > 1) {
                isExiste = true;               
                } 
        } else {
            isExiste = menuActionRepository.existsByDeletedFalseAndMenuActionCodeIgnoreCase(menuActionDto.getMenuActionCode());

        }
    
        if (isExiste.equals(Boolean.TRUE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce code existe déjà");
        }
        return isExiste;
        }
        
        
        
    
    

    
}

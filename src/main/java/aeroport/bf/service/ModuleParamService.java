package aeroport.bf.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.security.SecurityUtils;
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
import aeroport.bf.repository.UserRepository;



@Service
@Transactional
@RequiredArgsConstructor
public class ModuleParamService {
    private final ModuleParamRepository moduleParamRepository;
    private final ModuleParamMapper moduleParamMapper;
    private final MenuActionRepository menuActionRepository;
    private final MenuActionMapper menuActionMapper;
    private final TraceRepository traceRepository;
    private final UserRepository userRepository;
    private final TraceService traceService;


/**
 * Save moduleParam.
 *
 * @param moduleParamDto {@link aeroport.bf.dto.ModuleParamDto}
 * @return saved moduleParam object
 */
private ModuleParamDto saveModuleParam(final ModuleParamDto moduleParamDto) {
    ModuleParam moduleParam = moduleParamMapper.toEntity(moduleParamDto);
    moduleParam.setDeleted(Boolean.FALSE);


    ModuleParam savedModuleParam = moduleParamRepository.save(moduleParam);
    
    if(!moduleParamDto.getMenuActions().isEmpty()) {
        for(MenuActionDto dto: moduleParamDto.getMenuActions()) {
            MenuAction action = menuActionMapper.toEntity(dto);
            if (isExisteMenuByCode(dto)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Le code de  existe déjà.");
            }
            action.setDeleted(false);
            action.setModuleParam(savedModuleParam);
            MenuAction menu= menuActionRepository.save(action);
           
                // traceService.writeAuditEvent(menu, EntityAuditAction.CREATE);
        }
    }

    return moduleParamMapper.toDto(savedModuleParam);
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

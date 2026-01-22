package aeroport.bf.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.security.SecurityUtils;
import aeroport.bf.domain.MenuAction;
import aeroport.bf.domain.Trace;
import aeroport.bf.dto.MenuActionDto;
import aeroport.bf.dto.mapper.MenuActionMapper;
import aeroport.bf.repository.MenuActionRepository;
import aeroport.bf.repository.TraceRepository;
import aeroport.bf.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor

public class MenuActionService {

    private final MenuActionRepository menuActionRepository;
    private final MenuActionMapper menuActionMapper;
 
    private final UserRepository userRepository;


/**
 * Save menuAction.
 *
 * @param menuActionDto {@link bf.dgi.ast.dto.MenuActionDto}
 * @return saved menuAction object
 */
private MenuActionDto saveMenuAction(final MenuActionDto menuActionDto) {
    MenuAction menuAction = menuActionMapper.toEntity(menuActionDto);
    menuAction.setDeleted(Boolean.FALSE);

    MenuAction savedMenuAction = menuActionRepository.save(menuAction);

       
    /* if(menuActionDto.getId() !=null) {
        traceService.writeAuditEvent(savedMenuAction, EntityAuditAction.UPDATE);
       } else {
        traceService.writeAuditEvent(savedMenuAction, EntityAuditAction.CREATE);
       } */
    return menuActionMapper.toDto(savedMenuAction);
}

/**
 * Create new menuAction.
 *
 * @param menuActionDto {@link bf.dgi.ast.dto.MenuActionDto}
 * @return created menuAction object
 */
public MenuActionDto createMenuAction(final MenuActionDto menuActionDto) {
    if (isExisteByCode(menuActionDto)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code de ce produit existe déjà.");
    }

    return saveMenuAction(menuActionDto);
}

/**
 * Update existing menuAction.
 *
 * @param menuActionDto {@link bf.dgi.ast.dto.MenuActionDto}
 * @return updated menuAction object
 */
public MenuActionDto updateMenuAction(final MenuActionDto menuActionDto, final long id) {
    if (!menuActionRepository.existsByDeletedFalseAndId(id)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No menuAction exists with this ID : %d", id));
    }

    if (Objects.isNull(menuActionDto.getId())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created menuAction cannot have null ID.");
    }

    if (isExisteByCode(menuActionDto)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code de ce produit existe déjà.");
    }

    return saveMenuAction(menuActionDto);
}

/**
 * Get menuAction by id.
 *
 * @param id searched menuAction id
 * @return found menuAction object
 */
public MenuActionDto findMenuAction(final long id) {
    if (!menuActionRepository.existsByDeletedFalseAndId(id)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No menuAction exists with this ID : %d", id));
    }

    return menuActionMapper.toDto(menuActionRepository.findOneByDeletedFalseAndId(id));
}

/**
 * Fetch all menuAction stored in DB.
 *
 * @return list of {@link bf.dgi.ast.dto.MenuActionDto}
 */
public List<MenuActionDto> findAllMenuAction() {
    List<MenuAction> menuActions = menuActionRepository.findAllByDeletedFalse();
    if (menuActions.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found, Please create at least one menuAction before.");
    }
    return menuActions.stream().map(menu->menuActionMapper.toDto(menu)).collect(Collectors.toList());
}

/**
 * Remove a menuAction by id if exists.
 *
 * @param id removed menuAction id.
 */
public void deleteMenuAction(final long id) {
    menuActionRepository.findTop1ByDeletedFalseAndId(id).ifPresentOrElse(menuAction -> {
        menuAction.setDeleted(Boolean.TRUE);
        menuActionRepository.save(menuAction);
      //  traceService.writeAuditEvent(menuAction, EntityAuditAction.DELETE);
    },
    () -> {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove menuAction with ID : %d", id));
    });
}

/**
 * Remove a menuAction list.
 *
 * @param dtos removed menuAction.
 */
public List<MenuActionDto> deleteAll(List<MenuActionDto> dtos) {
    dtos.stream().map(menu->menuActionMapper.toEntity(menu)).peek(menuAction -> {
        menuAction.setDeleted(Boolean.TRUE);
        menuActionRepository.save(menuAction);
    

    }).collect(Collectors.toList());
    return dtos;
}


     public Boolean isExisteByCode(final MenuActionDto menuActionDto) {
        Boolean isExiste = menuActionRepository.existsByDeletedFalseAndMenuActionCodeIgnoreCase(menuActionDto.getMenuActionCode() );
    
        if (isExiste.equals(Boolean.TRUE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce code existe déjà");
        }
        
        return isExiste;
    }
    
}

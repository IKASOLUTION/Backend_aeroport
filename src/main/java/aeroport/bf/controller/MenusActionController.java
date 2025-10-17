package aeroport.bf.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import aeroport.bf.dto.MenuActionDto;
import aeroport.bf.service.MenuActionService;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tags(@Tag(name = "MenusAction", description = "Gestion des MenusAction"))

public class MenusActionController {

    private final MenuActionService menuActionService;

/**
 * POST  /menuActions  : Creates a new menuAction.
 *
 * @param menuActionDto
 * @return {@link aeroport.bf.dto.MenuActionDto}
 */
@PostMapping("/menuActions")
@Operation(summary = "Creating a new menuAction.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<MenuActionDto> createMenuAction(@Valid @RequestBody final MenuActionDto dto) {
    return ResponseEntity.ok(menuActionService.createMenuAction(dto));
}

/**
 * PUT  /menuActions/:id  : Updates an existing menuAction.
 *
 * @param menuActionDto
 * @param id
 * @return {@link aeroport.bf.dto.MenuActionDto}
 */
@PutMapping("/menuActions/{id}")
@Operation(summary = "Update an existing menuAction.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
        @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
        @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<MenuActionDto> updateMenuAction(@Valid @RequestBody final MenuActionDto dto, @PathVariable Long id) {
    return ResponseEntity.ok(menuActionService.updateMenuAction(dto, id));
}

/**
 * GET /menuActions : get all menuActions.
 *
 * @return {@link java.util.List<aeroport.bf.dto.MenuActionDto>}
 */
@GetMapping("/menuActions")
@Operation(summary = "Fetch all menuActions")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<List<MenuActionDto>> getAllMenuActions() {
    return new ResponseEntity<>(menuActionService.findAllMenuAction(), HttpStatus.OK);
}

/**
 * GET /menuActions/:id : get menuAction.
 *
 * @param id
 * @return {@link aeroport.bf.dto.MenuActionDto}
 */
@GetMapping("/menuActions/{id}")
@Operation(summary = "Get menuAction")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<MenuActionDto> findMenuAction(@PathVariable final Long id) {
    return ResponseEntity.ok(menuActionService.findMenuAction(id));
}

/**
 * DELETE /menuActions/:id : delete menuAction.
 *
 * @param id
 * @return {@link java.util.List<aeroport.bf.dto.MenuActionDto>}
 */
@DeleteMapping("/menuActions/{id}")
@Operation(summary = "Remove menuAction")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<Void> deleteMenuAction(@PathVariable final Long id) {
    menuActionService.deleteMenuAction(id);
    return ResponseEntity.noContent().build();
}

/**
 * DELETE /menuActions/all : delete a list of menuActions.
 *
 * @param dtos removed menuActions.
 * @return {@link java.util.List<aeroport.bf.dto.MenuActionDto>}
 */
@PutMapping("/menuActions/all")
@Operation(summary = "Delete all existing menuActions.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
        @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
        @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<List<MenuActionDto>> deleteAll(@RequestBody final List<MenuActionDto> dtos) {
    return ResponseEntity.ok(menuActionService.deleteAll(dtos));
}

    
}

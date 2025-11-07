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
import aeroport.bf.dto.ModuleParamDto;
import aeroport.bf.service.ModuleParamService;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tags(@Tag(name = "ModuleParam", description = "Gestion des ModuleParam"))

public class ModuleParamController {

    private final ModuleParamService moduleParamService;

/**
 * POST  /moduleParams  : Creates a new moduleParam.
 *
 * @param moduleParamDto
 * @return {@link aeroport.bf.dto.ModuleParamDto}
 */
@PostMapping("/moduleParams")
@Operation(summary = "Creating a new moduleParam.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<ModuleParamDto> createModuleParam(@Valid @RequestBody final ModuleParamDto dto) {
    System.out.println("dto ==============================");
    log.debug("REST request to save ModuleParam : {}", dto);
    return ResponseEntity.ok(moduleParamService.createModuleParam(dto));
}

/**
 * PUT  /moduleParams/:id  : Updates an existing ModuleParam.
 *
 * @param moduleParamDto
 * @param id
 * @return {@link aeroport.bf.dto.ModuleParamDto}
 */
@PutMapping("/moduleParams/{id}")
@Operation(summary = "Update an existing moduleParam.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
        @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
        @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<ModuleParamDto> updateModuleParam(@Valid @RequestBody final ModuleParamDto dto, @PathVariable Long id) {
    return ResponseEntity.ok(moduleParamService.updateModuleParam(dto, id));
}

/**
 * GET /moduleParams : get all moduleParams.
 *
 * @return {@link java.util.List<aeroport.bf.dto.ModuleParamDto>}
 */
@GetMapping("/moduleParams")
@Operation(summary = "Fetch all moduleParams")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<List<ModuleParamDto>> getAllModuleParams() {
    return new ResponseEntity<>(moduleParamService.findAllModuleParam(), HttpStatus.OK);
}

/**
 * GET /moduleParams/:id : get moduleParam.
 *
 * @param id
 * @return {@link aeroport.bf.dto.ModuleParamDto}
 */
@GetMapping("/moduleParams/{id}")
@Operation(summary = "Get moduleParam")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<ModuleParamDto> findModuleParam(@PathVariable final Long id) {
    return ResponseEntity.ok(moduleParamService.findModuleParam(id));
}


@GetMapping("/moduleParams/menus/{idModule}")
@Operation(summary = "Get moduleParam")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<List<MenuActionDto>> findMenuByModule(@PathVariable final Long idModule) {
    return ResponseEntity.ok(moduleParamService.findMenuByModule(idModule));
}

/**
 * DELETE /moduleParams/:id : delete moduleParam.
 *
 * @param id
 * @return {@link java.util.List<aeroport.bf.dto.ModuleParamDto>}
 */
@DeleteMapping("/moduleParams/{id}")
@Operation(summary = "Remove moduleParam")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<Void> deleteModuleParam(@PathVariable final Long id) {
    moduleParamService.deleteModuleParam(id);
    return ResponseEntity.noContent().build();
}

@PutMapping("/moduleParams/all")
@Operation(summary = "delete all an existing moduleParam.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
        @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
        @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
        @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
        @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
})
public ResponseEntity<List<ModuleParamDto>> deleteAll(@RequestBody final List<ModuleParamDto> dtos) {
    return ResponseEntity.ok(moduleParamService.deleteAll(dtos));
}

    
}

package aeroport.bf.controller;

import aeroport.bf.dto.ResponsableDto;
import aeroport.bf.service.ResponsableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tags(@Tag(name = "Responsable", description = "Gestion des responsables"))
public class ResponsableController {
    private final ResponsableService responsableService;

    /**
     * POST  /responsables  : Creates a new responsables.
     *
     * @param dto {@link ResponsableDto}
     * @return {@link ResponsableDto}
     */
    @PostMapping("/responsables")
    @Operation(summary = "Creating a new responsables.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ResponsableDto> create(@Valid @RequestBody final ResponsableDto dto) {
        
        return ResponseEntity.ok(responsableService.create(dto));
    }

    /**
     * PUT  /responsables/:id  : Updates an existing Responsables.
     *
     * @param dto
     * @param id
     * @return {@link ResponsableDto}
     */
    @PutMapping("/responsables/{id}")
    @Operation(summary = "Update an existing Responsables.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ResponsableDto> update(@Valid @RequestBody final ResponsableDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(responsableService.update(dto, id));
    }

    /**
     * GET / : get all responsables.
     *
     * @return {@link List<ResponsableDto>}
     */
    @GetMapping("/responsables")
    @Operation(summary = "Fetch all Pays")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<ResponsableDto>> getAll() {
        return new ResponseEntity<>(responsableService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get Responsable.
     *
     * @param id
     * @return {@link List<ResponsableDto>}
     */
    @GetMapping("/responsables/{id}")
    @Operation(summary = "Get Responsable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ResponsableDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(responsableService.findOne(id));
    }

    /**
     * DELETE /:id : delete Pays.
     *
     * @param id
     * @return {@link List<ResponsableDto>}
     */
    @DeleteMapping("/responsables/{id}")
    @Operation(summary = "Remove Pays")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        responsableService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

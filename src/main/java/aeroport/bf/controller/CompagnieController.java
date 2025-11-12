package aeroport.bf.controller;

import aeroport.bf.dto.CompagnieDto;
import aeroport.bf.service.CompagnieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tags(@Tag(name = "Compagnie", description = "Gestion des compagnies"))
public class CompagnieController {
    private final CompagnieService compagnieService;

    /**
     * POST  /compagnies  : Creates a new compagnies.
     *
     * @param dto {@link CompagnieDto}
     * @return {@link CompagnieDto}
     */
    @PostMapping("/compagnies")
    @Operation(summary = "Creating a new Compagnie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<CompagnieDto> create(@Valid @RequestBody final CompagnieDto dto) {
        
        return ResponseEntity.ok(compagnieService.create(dto));
    }

    /**
     * PUT  /compagnies/:id  : Updates an existing Compagnie.
     *
     * @param dto
     * @param id
     * @return {@link CompagnieDto}
     */
    @PutMapping("/compagnies/{id}")
    @Operation(summary = "Update an existing Compagnie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<CompagnieDto> update(@Valid @RequestBody final CompagnieDto dto,
                                              @PathVariable Long id) {
        return ResponseEntity.ok(compagnieService.update(dto, id));
    }

    /**
     * GET / : get all Compagnies.
     *
     * @return {@link List<CompagnieDto>}
     */
    @GetMapping("/compagnies")
    @Operation(summary = "Fetch all Compagnies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<CompagnieDto>> getAll() {
        return new ResponseEntity<>(compagnieService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get Compagnies.
     *
     * @param id
     * @return {@link List<CompagnieDto>}
     */
    @GetMapping("/compagnies/{id}")
    @Operation(summary = "Get Hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<CompagnieDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(compagnieService.findOne(id));
    }

    /**
     * DELETE /:id : delete Compagnie.
     *
     * @param id
     * @return {@link List<CompagnieDto>}
     */
    @DeleteMapping("/compagnies/{id}")
    @Operation(summary = "Remove CompagnieDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        compagnieService.delete(id);
        return ResponseEntity.noContent().build();
    }

   

}

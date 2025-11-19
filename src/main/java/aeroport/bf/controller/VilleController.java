package aeroport.bf.controller;

import aeroport.bf.dto.PaysDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.service.PaysService;
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
import org.springframework.web.bind.annotation.PatchMapping;
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
@Tags(@Tag(name = "Pays", description = "Gestion des Ville"))
public class VilleController {
    private final aeroport.bf.service.VilleService  villeService;

    /**
     * POST  /pays  : Creates a new pays.
     *
     * @param dto {@link VilleDto}
     * @return {@link VilleDto}
     */
    @PostMapping("/ville")
    @Operation(summary = "Creating a new Ville.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<VilleDto> create(@Valid @RequestBody final VilleDto dto) {
        
        return ResponseEntity.ok(villeService.create(dto));
    }

    /**
     * PUT  /ville/:id  : Updates an existing Pays.
     *
     * @param dto
     * @param id
     * @return {@link VilleDto}
     */
    @PutMapping("/ville/{id}")
    @Operation(summary = "Update an existing Ville.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<VilleDto> update(@Valid @RequestBody final VilleDto dto, @PathVariable Long id) {
        System.out.println("-------Afficher dto-----"+dto);
        System.out.println("------Afficher id----------"+id);
        return ResponseEntity.ok(villeService.update(dto, id));
    }

    /**
     * GET / : get all Ville.
     *
     * @return {@link List<VilleDto>}
     */
    @GetMapping("/ville")
    @Operation(summary = "Fetch all Ville")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<VilleDto>> getAll() {
        return new ResponseEntity<>(villeService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get Pays.
     *
     * @param id
     * @return {@link List<PaysDto>}
     */
    @GetMapping("/ville/{id}")
    @Operation(summary = "Get Pays")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<VilleDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(villeService.findOne(id));
    }

    /**
     * DELETE /:id : delete Vile.
     *
     * @param id
     * @return {@link List<VilleDto>}
     */
    @PatchMapping("/ville/{id}")
    @Operation(summary = "Remove Ville")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        villeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

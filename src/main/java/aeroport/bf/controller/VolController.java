package aeroport.bf.controller;

import aeroport.bf.config.util.PageableUtil;
import aeroport.bf.dto.SearchDto;
import aeroport.bf.dto.UserDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.dto.VolDto;
import aeroport.bf.service.VolService;

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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tags(@Tag(name = "vol", description = "Gestion des vols"))
public class VolController {
    private final VolService volService;

    /**
     * POST  /users  : Creates a new user.
     *
     * @param dto
     * @return {@link VolDto}
     */
    @PostMapping("/vols")
    @Operation(summary = "Creating a new Vol.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<VolDto> create(@Valid @RequestBody final VolDto dto) {
        return ResponseEntity.ok(volService.create(dto));
    }

    /**
     * PUT /vols/:id : Updates an existing vol.
     *
     * @param dto
     * @param id
     * @return {@link volDto}
     */
    @PutMapping("/vols/{id}")
    @Operation(summary = "Update an existing vol.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<VolDto> update(@Valid @RequestBody final VolDto dto,
            @PathVariable Long id) {
        return ResponseEntity.ok(volService.update(dto, id));
    }

    /**
     * GET / : get all vols.
     *
     * @return {@link List<VolDto>}
     */
    @GetMapping("/vols")
    @Operation(summary = "Fetch all vols")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<VolDto>> getAll() {
        return new ResponseEntity<>(volService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get vols.
     *
     * @param id
     * @return {@link List<VolDto>}
     */
    @GetMapping("/vols/{id}")
    @Operation(summary = "Get Hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<VolDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(volService.findOne(id));
    }

    /**
     * DELETE /:id : delete vol.
     *
     * @param id
     * @return {@link List<VolDto>}
     */
    @DeleteMapping("/vols/{id}")
    @Operation(summary = "Remove VolDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        volService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/vols/periode")
    public ResponseEntity<Page<VolDto>> getTachesHistorique(@RequestBody SearchDto search) {
        System.out.println("Received search request: " + search);
        // Validation
        if (search.getDateDebut() == null || search.getDateFin() == null) {
            throw new IllegalArgumentException("dateDebut et dateFin sont obligatoires");
        }

        if (search.getDateDebut().isAfter(search.getDateFin())) {
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
        }
        search.setSortBy("dateDepart");

        // Création du Pageable et récupération des données
        Pageable pageable = PageableUtil.fromSearchDto(search);
        System.out.println("Received search pageable: " + search);
        Page<VolDto> vols = volService.findAllPeriodeAndStatut(
                
                search.getDateDebut(),
                search.getDateFin(),
                search.getStatutVols(),
                pageable);
System.out.println("Received vols request: " + vols);
        return ResponseEntity.ok(vols);
    }


}

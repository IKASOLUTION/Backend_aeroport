package aeroport.bf.controller;

import aeroport.bf.config.util.PageableUtil;
import aeroport.bf.dto.SearchDto;
import aeroport.bf.dto.UserDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.dto.VolDto;
import aeroport.bf.dto.VoyageDto;
import aeroport.bf.service.VoyageService;

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
@Tags(@Tag(name = "voyage", description = "Gestion des voyages"))
public class VoyageController {
    private final VoyageService voyageService;

    /**
     * POST  /users  : Creates a new user.
     *
     * @param dto
     * @return {@link VoyageDto}
     */
    @PostMapping("/voyages")
    @Operation(summary = "Creating a new Voyage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<VoyageDto> create(@Valid @RequestBody final VoyageDto dto) {

        return ResponseEntity.ok(voyageService.create(dto));
    }
 
    /**
     * PUT /voyages/:id : Updates an existing voyage.
     *
     * @param dto
     * @param id
     * @return {@link voyageDto}
     */
    @PutMapping("/voyages/{id}")
    @Operation(summary = "Update an existing voyage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<VoyageDto> update(@Valid @RequestBody final VoyageDto dto,
            @PathVariable Long id) {
        return ResponseEntity.ok(voyageService.update(dto, id));
    }

    /**
     * GET / : get all voyages.
     *
     * @return {@link List<VoyageDto>}
     */
    @GetMapping("/voyages")
    @Operation(summary = "Fetch all voyages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<VoyageDto>> getAll() {
        return new ResponseEntity<>(voyageService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get voyages.
     *
     * @param id
     * @return {@link List<VoyageDto>}
     */
    @GetMapping("/voyages/{id}")
    @Operation(summary = "Get Hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<VoyageDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(voyageService.findOne(id));
    }

    /**
     * DELETE /:id : delete voyage.
     *
     * @param id
     * @return {@link List<VoyageDto>}
     */
    @DeleteMapping("/voyages/{id}")
    @Operation(summary = "Remove VoyageDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        voyageService.delete(id);
        return ResponseEntity.noContent().build();
    }

     @PutMapping("/voyages/periode")
    public ResponseEntity<Page<VoyageDto>> getTachesHistorique(@RequestBody SearchDto search) {
        System.out.println("Received search request: " + search);
        // Validation
        if (search.getDateDebut() == null || search.getDateFin() == null) {
            throw new IllegalArgumentException("dateDebut et dateFin sont obligatoires");
        }

        if (search.getDateDebut().isAfter(search.getDateFin())) {
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
        }
        search.setSortBy("dateVoyage");

        // Création du Pageable et récupération des données
        Pageable pageable = PageableUtil.fromSearchDto(search);
        System.out.println("Received search pageable: " + search);
        Page<VoyageDto> voyages = voyageService.findAllPeriodeAndStatut(
                search.getDateDebut(),
                search.getDateFin(),
                search.getStatutVoyages(),
                pageable);
System.out.println("Received vols request: " + voyages);
        return ResponseEntity.ok(voyages);
    }


}



package aeroport.bf.controller;

import aeroport.bf.dto.MotifVoyageDto;
import aeroport.bf.service.MotifVoyageService;
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
@Tags(@Tag(name = "Motif", description = "Gestion des motifs"))
public class MotifVoyageController {
    private final MotifVoyageService motifVoyageService;

    /**
     * POST  /motif-voyages  : Creates a new motif.
     *
     * @param dto {@link MotifVoyageDto}
     * @return {@link MotifVoyageDto}
     */
    @PostMapping("/motif-voyages")
    @Operation(summary = "Creating a new motif.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<MotifVoyageDto> create(@Valid @RequestBody final MotifVoyageDto dto) {
        
        return ResponseEntity.ok(motifVoyageService.create(dto));
    }

    /**
     * PUT  /motif-voyages/:id  : Updates an existing otif.
     *
     * @param dto
     * @param id
     * @return {@link MotifVoyageDto}
     */
    @PutMapping("/motif-voyages/{id}")
    @Operation(summary = "Update an existing motif.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<MotifVoyageDto> update(@Valid @RequestBody final MotifVoyageDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(motifVoyageService.update(dto, id));
    }

    /**
     * GET / : get all motifs.
     *
     * @return {@link List<MotifVoyageDto>}
     */
    @GetMapping("/motif-voyages")
    @Operation(summary = "Fetch all motifs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<MotifVoyageDto>> getAll() {
        return new ResponseEntity<>(motifVoyageService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get motif.
     *
     * @param id
     * @return {@link List<MotifVoyageDto>}
     */
    @GetMapping("/motif-voyages/{id}")
    @Operation(summary = "Get motifs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<MotifVoyageDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(motifVoyageService.findOne(id));
    }

    /**
     * DELETE /:id : delete motif.
     *
     * @param id
     * @return {@link List<MotifVoyageDto>}
     */
    @DeleteMapping("/motif-voyages/{id}")
    @Operation(summary = "Remove motif")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        motifVoyageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

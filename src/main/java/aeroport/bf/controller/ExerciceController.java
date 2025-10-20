package aeroport.bf.controller;


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

import aeroport.bf.dto.ExerciceDto;
import aeroport.bf.service.ExerciceService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tags(@Tag(name = "Exercice", description = "Gestion des commune"))
public class ExerciceController {
    private final ExerciceService exerciceService;

    /**
     * POST  /communes  : Creates a new commune.
     *
     * @param dto {@link ExerciceDto}
     * @return {@link ExerciceDto}
     */
    @PostMapping("/exerices")
    @Operation(summary = "Creating a new commune.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ExerciceDto> create(@Valid @RequestBody final ExerciceDto dto) {
        return ResponseEntity.ok(exerciceService.createExercice(dto));
    }

    /**
     * PUT  /communes/:id  : Updates an existing commune.
     *
     * @param dto
     * @param id
     * @return {@link ExerciceDto}
     */
    @PutMapping("/exercices/{id}")
    @Operation(summary = "Update an existing province.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ExerciceDto> update(@Valid @RequestBody final ExerciceDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(exerciceService.updateExercice(dto, id));
    }

    /**
     * GET / : get all exercices.
     *
     * @return {@link List<ExerciceDto>}
     */
    @GetMapping("/exercices")
    @Operation(summary = "Fetch all exercices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<ExerciceDto>> getAll() {
        return new ResponseEntity<>(exerciceService.findAllExercice(), HttpStatus.OK);
    }

    /**
     * GET /:id : get commune.
     *
     * @param id
     * @return {@link List<ExerciceDto>}
     */
    @GetMapping("/exercices/{id}")
    @Operation(summary = "Get exercice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ExerciceDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(exerciceService.findExercice(id));
    }

    /**
     * DELETE /:id : delete commune.
     *
     * @param id
     * @return {@link List<RegionDto>}
     */
    @DeleteMapping("/exercices/{id}")
    @Operation(summary = "Remove xercice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        exerciceService.deleteExercice(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/exercices/active-desactive-exercice/{id}")
    @Operation(summary = "Update an existing ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ExerciceDto> activeExercice(@Valid @RequestBody final ExerciceDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(exerciceService.ActiveExercice(dto, id));
    }

}

package aeroport.bf.controller;

import aeroport.bf.dto.PaysDto;
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
@Tags(@Tag(name = "Pays", description = "Gestion des pays"))
public class PaysController {
    private final PaysService paysService;

    /**
     * POST  /pays  : Creates a new pays.
     *
     * @param dto {@link PaysDto}
     * @return {@link PaysDto}
     */
    @PostMapping("/pays")
    @Operation(summary = "Creating a new Pays.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<PaysDto> create(@Valid @RequestBody final PaysDto dto) {
        
        return ResponseEntity.ok(paysService.create(dto));
    }

    /**
     * PUT  /pays/:id  : Updates an existing Pays.
     *
     * @param dto
     * @param id
     * @return {@link PaysDto}
     */
    @PutMapping("/pays/{id}")
    @Operation(summary = "Update an existing Pays.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<PaysDto> update(@Valid @RequestBody final PaysDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(paysService.update(dto, id));
    }

    /**
     * GET / : get all Pays.
     *
     * @return {@link List<PaysDto>}
     */
    @GetMapping("/pays")
    @Operation(summary = "Fetch all Pays")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<PaysDto>> getAll() {
        return new ResponseEntity<>(paysService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get Pays.
     *
     * @param id
     * @return {@link List<PaysDto>}
     */
    @GetMapping("/pays/{id}")
    @Operation(summary = "Get Pays")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<PaysDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(paysService.findOne(id));
    }

    /**
     * DELETE /:id : delete Pays.
     *
     * @param id
     * @return {@link List<PaysDto>}
     */
    @DeleteMapping("/pays/{id}")
    @Operation(summary = "Remove Pays")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        paysService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

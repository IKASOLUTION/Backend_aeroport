package aeroport.bf.controller;

import aeroport.bf.dto.AeroportDto;
import aeroport.bf.service.AeroportService;
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
import org.springframework.web.bind.annotation.PatchMapping;
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
@Tags(@Tag(name = "Aeroport", description = "Gestion des Aeroports"))
public class AeroportController {
    private final AeroportService aeroportService;

    /**
     * POST  /Aeroports  : Creates a new Aeroports.
     *
     * @param dto {@link AeroportDto}
     * @return {@link AeroportDto}
     */
    @PostMapping("/aeroports")
    @Operation(summary = "Creating a new Aeroport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<AeroportDto> create(@Valid @RequestBody final AeroportDto dto) {
         System.out.println("addddd Aeroport" + dto);
        return ResponseEntity.ok(aeroportService.create(dto));
    }

    /**
     * PUT  /Aeroports/:id  : Updates an existing Aeroport.
     *
     * @param dto
     * @param id
     * @return {@link aeroportDto}
     */
    @PutMapping("/aeroports/{id}")
    @Operation(summary = "Update an existing Aeroport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<AeroportDto> update(@Valid @RequestBody final AeroportDto dto,
                                              @PathVariable Long id) {
        return ResponseEntity.ok(aeroportService.update(dto, id));
    }

    /**
     * GET / : get all Aeroports.
     *
     * @return {@link List<AeroportDto>}
     */
    @GetMapping("/aeroports")
    @Operation(summary = "Fetch all Aeroports")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<AeroportDto>> getAll() {
        return new ResponseEntity<>(aeroportService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get Aeroports.
     *
     * @param id
     * @return {@link List<AeroportDto>}
     */
    @GetMapping("/aeroports/{id}")
    @Operation(summary = "Get Hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<AeroportDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(aeroportService.findOne(id));
    }

    /**
     * DELETE /:id : delete Aeroport.
     *
     * @param id
     * @return {@link List<AeroportDto>}
     */
    @PatchMapping("/aeroports/{id}")
    @Operation(summary = "Remove AeroportDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        aeroportService.delete(id);
        return ResponseEntity.noContent().build();
    }

  

}

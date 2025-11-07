package aeroport.bf.controller;

import aeroport.bf.dto.ListeNoireDto;
import aeroport.bf.service.ListeNoireService;
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
@Tags(@Tag(name = "ListeNoire", description = "Gestion des listes noire"))
public class ListeNoireController {
    private final ListeNoireService listeNoireService;

    /**
     * POST  /liste-noires : Creates a new Liste noire.
     *
     * @param dto {@link ListeNoireDto}
     * @return {@link ListeNoireDto}
     */
    @PostMapping("/liste-noires")
    @Operation(summary = "Creating a new liste noire.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ListeNoireDto> create(@Valid @RequestBody final ListeNoireDto dto) {
        
        return ResponseEntity.ok(listeNoireService.create(dto));
    }

    /**
     * PUT  /liste-noires/:id  : Updates an existing liste noire.
     *
     * @param dto
     * @param id
     * @return {@link ListeNoireDto}
     */
    @PutMapping("/liste-noires/{id}")
    @Operation(summary = "Update an existing liste noire.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ListeNoireDto> update(@Valid @RequestBody final ListeNoireDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(listeNoireService.update(dto,id));
    }

    /**
     * GET / : get all Liste noire.
     *
     * @return {@link List<ListeNoireDto>}
     */
    @GetMapping("/liste-noires")
    @Operation(summary = "Fetch all liste noires")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<ListeNoireDto>> getAll() {
        return new ResponseEntity<>(listeNoireService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get liste noire.
     *
     * @param id
     * @return {@link List<ListeNoireDto>}
     */
    @GetMapping("/liste-noires/{id}")
    @Operation(summary = "Get liste noire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ListeNoireDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(listeNoireService.findOne(id));
    }

    /**
     * DELETE /:id : delete liste noire.
     *
     * @param id
     * @return {@link List<ListeNoireDto>}
     */
    @DeleteMapping("/liste-noires/{id}")
    @Operation(summary = "Remove liste noire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        listeNoireService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/liste-noires/criteria")
    @Operation(summary = "fech by page an existing liste noir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<ListeNoireDto>> getWithCriteria(final ListeNoireDto dto , Pageable pageable) {
        Page<ListeNoireDto> page = listeNoireService.findByPage(dto,pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

}

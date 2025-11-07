package aeroport.bf.controller;

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

import aeroport.bf.dto.ProfilDto;
import aeroport.bf.service.ProfilService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tags(@Tag(name = "Profils", description = "Gestion des profiles"))
public class ProfilController {
    private final ProfilService service;

    /**
     * POST  /tickets  : Creates a new ticket.
     *
     * @param ticketDto
     * @return {@link aeroport.bf.dto.TicketDto}
     */
    @PostMapping("/profils")
    @Operation(summary = "Creating a new profil.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ProfilDto> create(@Valid @RequestBody final ProfilDto dto) {
        
        return ResponseEntity.ok(service.createProfil(dto));
    }

    /**
     * PUT  /tickets/:id  : Updates an existing Ticket.
     *
     * @param ticketDto
     * @param id
     * @return {@link aeroport.bf.dto.TicketDto}
     */
    @PutMapping("/profils/{id}")
    @Operation(summary = "Update an existing ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ProfilDto> update(@Valid @RequestBody final ProfilDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(service.update(dto, id));
    }

    /**
     * GET / : get all tickets.
     *
     * @return {@link java.util.List<aeroport.bf.dto.TicketDto>}
     */
    @GetMapping("/profils")
    @Operation(summary = "Fetch all tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<ProfilDto>> getAllProfils() {
        return new ResponseEntity<>(service.findAllProfil(), HttpStatus.OK);
    }

    /**
     * GET /:id : get ticket.
     *
     * @param id
     * @return {@link java.util.List<aeroport.bf.dto.TicketDto>}
     */
    @GetMapping("/profils/{id}")
    @Operation(summary = "Get ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<ProfilDto> findProfil(@PathVariable final Long id) {
        return ResponseEntity.ok(service.findProfil(id));
    }

    /**
     * DELETE /:id : delete ticket.
     *
     * @param id
     * @return {@link java.util.List<aeroport.bf.dto.TicketDto>}
     */
    @DeleteMapping("/profils/{id}")
    @Operation(summary = "Remove ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profils/all")
    @Operation(summary = "delete all an existing profil.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<ProfilDto>> deleteAll(@RequestBody final List<ProfilDto> dtos) {
        return ResponseEntity.ok(service.deleteAll(dtos));
    }

}

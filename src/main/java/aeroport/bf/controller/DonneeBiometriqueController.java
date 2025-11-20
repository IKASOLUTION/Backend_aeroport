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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import aeroport.bf.dto.DonneeBiometriqueDto;
import aeroport.bf.dto.InformationPersonnelleDto;
import aeroport.bf.service.DonneeBiometriqueService;

import java.io.File;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tags(@Tag(name = "DonneeBiometriques", description = "Gestion des donneeBiometriquees"))
public class DonneeBiometriqueController {
    private final DonneeBiometriqueService service;

    /**
     * POST /tickets : Creates a new ticket.
     *
     * @param ticketDto
     * @return {@link aeroport.bf.dto.TicketDto}
     */
    @PostMapping(path = "/donneeBiometriques", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Creating a new donneeBiometrique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<DonneeBiometriqueDto> create(
            @RequestPart(value = "photoBiometrique", required = false) MultipartFile photoBiometrique,
            @RequestParam(value = "informationPersonnelleId", required = true) Long informationPersonnelleId,
            @RequestParam(value = "empreintePouces", required = true) MultipartFile empreintePouces,
            @RequestParam(value = "empreinteGauche", required = true) MultipartFile empreinteGauche,
            @RequestParam(value = "empreinteDroite", required = true) MultipartFile empreinteDroite) {
                System.out.println("informationPersonnelleId: " + informationPersonnelleId);
                System.out.println("empreintePouces: " + empreintePouces);
                System.out.println("empreinteGauche: " + empreinteGauche);  
        DonneeBiometriqueDto dto = DonneeBiometriqueDto.builder()
                .informationPersonnelleId(informationPersonnelleId)
                .empreintePouces(empreintePouces)
                .empreinteGauche(empreinteGauche)
                .empreinteDroite(empreinteDroite)
                .photoBiometrique(photoBiometrique)
                .build();

        return ResponseEntity.ok(service.createDonneeBiometrique(dto));
    }

    /**
     * PUT /tickets/:id : Updates an existing Ticket.
     *
     * @param ticketDto
     * @param id
     * @return {@link aeroport.bf.dto.TicketDto}
     */
    @PutMapping("/donneeBiometriques/{id}")
    @Operation(summary = "Update an existing ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<DonneeBiometriqueDto> update(@Valid @RequestBody final DonneeBiometriqueDto dto,
            @PathVariable Long id) {
        return ResponseEntity.ok(service.update(dto, id));
    }

    /**
     * GET / : get all tickets.
     *
     * @return {@link java.util.List<aeroport.bf.dto.TicketDto>}
     */
    @GetMapping("/donneeBiometriques")
    @Operation(summary = "Fetch all tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<DonneeBiometriqueDto>> getAllDonneeBiometriques() {
        return new ResponseEntity<>(service.findAllDonneeBiometrique(), HttpStatus.OK);
    }

    /**
     * GET /:id : get ticket.
     *
     * @param id
     * @return {@link java.util.List<aeroport.bf.dto.TicketDto>}
     */
    @GetMapping("/donneeBiometriques/{id}")
    @Operation(summary = "Get ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<DonneeBiometriqueDto> findDonneeBiometrique(@PathVariable final Long id) {
        return ResponseEntity.ok(service.findDonneeBiometrique(id));
    }

    /**
     * DELETE /:id : delete ticket.
     *
     * @param id
     * @return {@link java.util.List<aeroport.bf.dto.TicketDto>}
     */
    @DeleteMapping("/donneeBiometriques/{id}")
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

    @PutMapping("/donneeBiometriques/all")
    @Operation(summary = "delete all an existing donneeBiometrique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<DonneeBiometriqueDto>> deleteAll(@RequestBody final List<DonneeBiometriqueDto> dtos) {
        return ResponseEntity.ok(service.deleteAll(dtos));
    }


    @GetMapping("/donneeBiometriques/personne")
    @Operation(summary = "Fetch all tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<InformationPersonnelleDto>> findAllPersonne() {
        return new ResponseEntity<>(service.findAllPersonne(), HttpStatus.OK);
    }
}

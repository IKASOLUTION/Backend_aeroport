package aeroport.bf.controller;

import aeroport.bf.config.util.PageableUtil;
import aeroport.bf.domain.enums.EtatVoyage;
import aeroport.bf.domain.enums.MotifVoyage;
import aeroport.bf.domain.enums.TypeDocument;
import aeroport.bf.dto.SearchDto;
import aeroport.bf.dto.UserDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.dto.EnregistrementDto;
import aeroport.bf.service.EnregistrementService;

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
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tags(@Tag(name = "enregistrement", description = "Gestion des enregistrements"))
public class EnregistrementController {
    private final EnregistrementService enregistrementService;

    /**
     * POST /users : Creates a new user.
     *
     * @param dto
     * @return {@link EnregistrementDto}
     */
    @PostMapping(path = "/enregistrements", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Creating a new Enregistrement.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<EnregistrementDto> create( // Fichiers
            @RequestPart(value = "photoProfil", required = false) MultipartFile photoProfil,
            @RequestPart(value = "imageRecto", required = false) MultipartFile imageRecto,
            @RequestPart(value = "imageVerso", required = false) MultipartFile imageVerso,

            // Champs simples
            @RequestParam(value = "typeDocument", required = false) String typeDocument,
            @RequestParam(value = "numeroDocument", required = false) String numeroDocument,
            @RequestParam(value = "numeroNip", required = false) String numeroNip,
            @RequestParam(value = "dateDelivrance", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDelivrance,
            @RequestParam(value = "lieuDelivrance", required = false) String lieuDelivrance,

            // Personal Info
            @RequestParam(value = "nomFamille", required = false) String nomFamille,
            @RequestParam(value = "prenom", required = false) String prenom,
            @RequestParam(value = "dateNaissance", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissance,
            @RequestParam(value = "lieuNaissance", required = false) String lieuNaissance,
            @RequestParam(value = "nationalite", required = false) String nationalite,
            @RequestParam(value = "profession", required = false) String profession,

            // Coordonnees
            @RequestParam(value = "paysResidence", required = false) String paysResidence,
            @RequestParam(value = "emailContact", required = false) String emailContact,
            @RequestParam(value = "telephoneBurkina", required = false) String telephoneBurkina,
            @RequestParam(value = "telephoneEtranger", required = false) String telephoneEtranger,
            @RequestParam(value = "adresseBurkina", required = false) String adresseBurkina,
            @RequestParam(value = "adresseEtranger", required = false) String adresseEtranger,
            @RequestParam(value = "dureeSejour", required = false) int dureeSejour,
            @RequestParam(value = "etatVoyage", required = false) EtatVoyage etatVoyage,
            @RequestParam(value = "motifVoyage", required = false) MotifVoyage motifVoyage,

            // Voyage
            @RequestParam(value = "volId", required = false) Long volId

    ) {
        System.out.println("Creating Enregistrement for: " + typeDocument + " ");

        // Construire le DTO
        EnregistrementDto dto = EnregistrementDto.builder()
                .photoProfil(photoProfil)
                .imageRecto(imageRecto)
                .imageVerso(imageVerso)
                .typeDocument(TypeDocument.valueOf(typeDocument.toUpperCase()))
                .numeroDocument(numeroDocument)
                .numeroNip(numeroNip)
                .dateDelivrance(dateDelivrance)
                .lieuDelivrance(lieuDelivrance)
                .nomFamille(nomFamille)
                .prenom(prenom)
                .dateNaissance(dateNaissance)
                .lieuNaissance(lieuNaissance)
                .nationalite(nationalite)
                .profession(profession)
                .paysResidence(paysResidence)
                .emailContact(emailContact)
                .telephoneBurkina(telephoneBurkina)
                .telephoneEtranger(telephoneEtranger)
                .adresseBurkina(adresseBurkina)
                .adresseEtranger(adresseEtranger)
                .volId(volId)
                .dureeSejour(dureeSejour)
                .etatVoyage(etatVoyage)
                .motifVoyage(motifVoyage)
                .build();
        System.out.println("Creating EnregistrementDto for: " + dto);
        EnregistrementDto saved = enregistrementService.create(dto);

        return ResponseEntity.ok(saved);
    }

    /**
     * PUT /enregistrements/:id : Updates an existing enregistrement.
     *
     * @param dto
     * @param id
     * @return {@link enregistrementDto}
     */
    @PutMapping("/enregistrements/{id}")
    @Operation(summary = "Update an existing enregistrement.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "409", description = "${swagger.http-status.409}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<EnregistrementDto> update(@Valid @RequestBody final EnregistrementDto dto,
            @PathVariable Long id) {
        return ResponseEntity.ok(enregistrementService.update(dto, id));
    }

    /**
     * GET / : get all enregistrements.
     *
     * @return {@link List<EnregistrementDto>}
     */
    @GetMapping("/enregistrements")
    @Operation(summary = "Fetch all enregistrements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<EnregistrementDto>> getAll() {
        return new ResponseEntity<>(enregistrementService.findAll(), HttpStatus.OK);
    }

    /**
     * GET /:id : get enregistrements.
     *
     * @param id
     * @return {@link List<EnregistrementDto>}
     */
    @GetMapping("/enregistrements/{id}")
    @Operation(summary = "Get Hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<EnregistrementDto> findOne(@PathVariable final Long id) {
        return ResponseEntity.ok(enregistrementService.findOne(id));
    }

    /**
     * DELETE /:id : delete enregistrement.
     *
     * @param id
     * @return {@link List<EnregistrementDto>}
     */
    @DeleteMapping("/enregistrements/{id}")
    @Operation(summary = "Remove EnregistrementDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        enregistrementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/enregistrements/periode")
    public ResponseEntity<Page<EnregistrementDto>> findAllPeriodeAndStatut(@RequestBody SearchDto search) {
        // Validation
        if (search.getDateDebut() == null || search.getDateFin() == null) {
            throw new IllegalArgumentException("dateDebut et dateFin sont obligatoires");
        }

        if (search.getDateDebut().isAfter(search.getDateFin())) {
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
        }

        search.setSortBy("dateSaisie");
        // Création du Pageable et récupération des données
        Pageable pageable = PageableUtil.fromSearchDto(search);
        Page<EnregistrementDto> taches = enregistrementService.findAllPeriodeAndStatut(

                search.getDateDebut(),
                search.getDateFin(),
                search.getAeroportId(),
                search.getStatus(),
                pageable);

        return ResponseEntity.ok(taches);
    }

    @PutMapping("/enregistrements/periode/voyageur-attente")
    public ResponseEntity<Page<EnregistrementDto>> findAllPeriodeVoyageurAttentAndStatut(
            @RequestBody SearchDto search) {
        // Validation
        if (search.getDateDebut() == null || search.getDateFin() == null) {
            throw new IllegalArgumentException("dateDebut et dateFin sont obligatoires");
        }

        if (search.getDateDebut().isAfter(search.getDateFin())) {
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
        }

        search.setSortBy("dateSaisie");
        // Création du Pageable et récupération des données
        Pageable pageable = PageableUtil.fromSearchDto(search);
        Page<EnregistrementDto> taches = enregistrementService.findAllPeriodeAndVoyageurAndStatut(

                search.getDateDebut(),
                search.getDateFin(),
                search.getAeroportId(),
                // search.getStatus(),
                pageable);

        return ResponseEntity.ok(taches);
    }

    /**
     * GET / : get all enregistrements.
     *
     * @return {@link List<EnregistrementDto>}
     */

    @GetMapping("/enregistrements/document/{numeroDocument}")
    public ResponseEntity<List<EnregistrementDto>> ListVol(@PathVariable String numeroDocument) {
        return new ResponseEntity<>(enregistrementService.ListVol(numeroDocument), HttpStatus.OK);
    }

}
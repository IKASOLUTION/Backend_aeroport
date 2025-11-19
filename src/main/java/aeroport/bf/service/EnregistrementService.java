package aeroport.bf.service;

import jakarta.transaction.Transactional;
import java_cup.runtime.lr_parser;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.config.security.SecurityUtils;
import aeroport.bf.domain.Ville;
import aeroport.bf.domain.Vol;
import aeroport.bf.domain.Voyage;
import aeroport.bf.domain.enums.EtatVoyage;
import aeroport.bf.domain.enums.MotifVoyage;
import aeroport.bf.domain.enums.Statut;
import aeroport.bf.domain.enums.StatutVoyage;
import aeroport.bf.domain.enums.StatutVoyageur;
import aeroport.bf.domain.enums.TypeDocument;
import aeroport.bf.domain.Enregistrement;
import aeroport.bf.domain.InformationPersonnelle;
import aeroport.bf.domain.PieceJointe;
import aeroport.bf.domain.User;
import aeroport.bf.dto.MenuActionDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.dto.EnregistrementDto;
import aeroport.bf.dto.mapper.VilleMapper;
import aeroport.bf.dto.mapper.EnregistrementMapper;
import aeroport.bf.dto.mapper.InformationPersonnelleMapper;
import aeroport.bf.repository.VilleRepository;
import aeroport.bf.repository.VolRepository;
import aeroport.bf.repository.VoyageRepository;
import aeroport.bf.repository.EnregistrementRepository;
import aeroport.bf.repository.InformationPersonnelleRepository;
import aeroport.bf.repository.PieceJointeRepository;
import aeroport.bf.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EnregistrementService {
    private final EnregistrementRepository enregistrementRepository;
    private final EnregistrementMapper enregistrementMapper;
    private final TraceService traceService;
    private final VilleRepository villeRepository;
    private final InformationPersonnelleRepository informationPersonnelleRepository;
    private final VoyageRepository voyageRepository;
    private final UserRepository userRepository;
    private final VolRepository volRepository;
    @Value("${files.upload.baseDir}/identite")
    private String uploadDirectory;
    private final PieceJointeRepository pieceJointeRepository;
    private final InformationPersonnelleMapper informationPersonnelleMapper;

    public EnregistrementDto create(final EnregistrementDto dto) {

        // === 1. Créer et enregistrer InformationPersonnelle ===
        InformationPersonnelle infoPerso = new InformationPersonnelle();
        infoPerso.setNomFamille(dto.getNomFamille());
        infoPerso.setPrenom(dto.getPrenom());
        infoPerso.setDateNaissance(dto.getDateNaissance());
        infoPerso.setLieuNaissance(dto.getLieuNaissance());
        infoPerso.setNationalite(dto.getNationalite());
        infoPerso.setTypeDocument(dto.getTypeDocument());
        infoPerso.setNumeroDocument(dto.getNumeroDocument());
        infoPerso.setDateDelivrance(dto.getDateDelivrance());
        infoPerso.setLieuDelivrance(dto.getLieuDelivrance());
        infoPerso.setNumeroNip(dto.getNumeroNip());
        infoPerso.setProfession(dto.getProfession());
        infoPerso.setAdresseBurkina(dto.getAdresseBurkina());
        infoPerso.setTelephoneBurkina(dto.getTelephoneBurkina());
        infoPerso.setPaysResidence(dto.getPaysResidence());
        infoPerso.setEmailContact(dto.getEmailContact());
        infoPerso.setStatut(Statut.ACTIF);
        infoPerso.setDateSaisie(LocalDate.now());
        System.out.println("DTO Nom Famille: " + infoPerso);
        InformationPersonnelle savedInfoPerso = informationPersonnelleRepository.save(infoPerso);
        System.out.println("DTO Nom savedInfoPerso: " + savedInfoPerso);
        // === 2. Créer et enregistrer Voyage ===
        Voyage voyage = new Voyage();
        System.out.println("DTO Nom dto.getVolId(): " + dto.getVolId());
        Vol vol = volRepository.getReferenceById(dto.getVolId());
        voyage.setVol(vol);
        System.out.println(
                "DTO Nom voyage avant setVol: " + villeRepository.getReferenceById(vol.getVilleDepart().getId()));
        voyage.setVilleDepart(villeRepository.getReferenceById(vol.getVilleDepart().getId()));
        voyage.setVilleDestination(villeRepository.getReferenceById(vol.getVilleArrivee().getId()));
        voyage.setMotifVoyage(dto.getMotifVoyage());
        voyage.setDateVoyage(dto.getDateVoyage());
        voyage.setHeureVoyage(LocalTime.now());
        voyage.setEtatVoyage(EtatVoyage.ALLER);
        voyage.setDureeSejour(dto.getDureeSejour());
        voyage.setStatut(StatutVoyage.ACTIF);
        System.out.println("DTO Nom voyage: " + voyage);
        Voyage savedVoyage = voyageRepository.save(voyage);
        System.out.println("DTO Nom savedVoyage: " + savedVoyage);
        // === 3. Créer Enregistrement ===
        Enregistrement enregistrement = new Enregistrement();
        SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByDeletedFalseAndUsername)
                .ifPresent(user -> {
                    enregistrement.setUtilisateur(user);
                });

        enregistrement.setInformationPersonnel(savedInfoPerso);
        enregistrement.setVoyage(savedVoyage);
        enregistrement.setAdresseEtranger(dto.getAdresseEtranger());
        enregistrement.setTelephoneEtranger(dto.getTelephoneEtranger());
        enregistrement.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutVoyageur.EN_ATTENTE);
        enregistrement.setDateSaisie(LocalDate.now());
        System.out.println("DTO Nom enregistrement: " + enregistrement);
        // === 4. Sauvegarder Enregistrement ===
        Enregistrement savedEnregistrement = enregistrementRepository.save(enregistrement);
        PieceJointe jointe = new PieceJointe();
        jointe.setInformationPersonnel(savedInfoPerso);
        if (dto.getImageRecto() != null) {

            jointe = uploadFileMultiple(dto.getImageRecto(), savedInfoPerso, 1, jointe);
        }

        if (dto.getImageVerso() != null) {

            jointe = uploadFileMultiple(dto.getImageVerso(), savedInfoPerso, 2, jointe);
        }

        if (dto.getPhotoProfil() != null) {

            jointe = uploadFileMultiple(dto.getPhotoProfil(), savedInfoPerso, 3, jointe);
        }

        pieceJointeRepository.save(jointe);

        System.out.println("DTO Nom savedEnregistrement: " + savedEnregistrement);
        EnregistrementDto dtoResult = new EnregistrementDto();
        dtoResult.setId(savedEnregistrement.getId());
        dtoResult.setNomFamille(savedEnregistrement.getInformationPersonnel().getNomFamille());
        dtoResult.setPrenom(savedEnregistrement.getInformationPersonnel().getPrenom());
        dtoResult.setInformationPersonnelleId(savedEnregistrement.getInformationPersonnel().getId());
        dtoResult.setNumeroDocument(savedEnregistrement.getInformationPersonnel().getNumeroDocument());
        dtoResult.setInformationPersonnelle(informationPersonnelleMapper.toDto(savedEnregistrement.getInformationPersonnel()));
        // === 5. Retourner le DTO ===
        return dtoResult;
    }

    /**
     * Update existing Enregistrement.
     *
     * @param dto {@link EnregistrementDto}
     * @param id
     * @return updated Enregistrement object
     */
    public EnregistrementDto update(final EnregistrementDto dto, final long id) {

        if (!enregistrementRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No Enregistrement exists with this ID : %d", id));
        }
        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Already created Enregistrement cannot have null ID.");
        }
        Enregistrement enregistrement = enregistrementMapper.toEntity(dto);

        return enregistrementMapper.toDto(enregistrementRepository.save(enregistrement));
    }

    /**
     * Get Enregistrement by id.
     *
     * @param id searched Hotel id
     * @return found EnregistrementDto object
     */
    public EnregistrementDto findOne(final long id) {
        if (!enregistrementRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No Enregistrement exists with this ID : %d", id));
        }
        return enregistrementMapper.toDto(Objects.requireNonNull(enregistrementRepository.findById(id).orElse(null)));
    }

    /**
     * Fetch page Enregistrement stored in DB.
     * 
     * @param dto of {@link EnregistrementDto}
     * @return page of {@link EnregistrementDto}
     */

    /**
     * Fetch all Enregistrement stored in DB.
     * 
     * @return list of {@link EnregistrementDto}
     */
    public List<EnregistrementDto> findAll() {
        return enregistrementMapper.toDto(enregistrementRepository.findAllByDeletedFalse());

    }

    public Page<EnregistrementDto> findAllPeriodeAndVoyageurAndStatut(LocalDate startDate, LocalDate endDate,
            Long aeroportId,
            Pageable pageable) {
        List<StatutVoyageur> statuts = Arrays.asList(

                StatutVoyageur.EN_ATTENTE);
        Page<Enregistrement> enregistrements = enregistrementRepository.findByFilters(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59),
                aeroportId,
                statuts,
                pageable);

       
        // Créer une liste sans doublons avec le nombre de voyages
        List<EnregistrementDto> uniqueEnregistrements = enregistrements.getContent()
                .stream()
               .map(enregistrement -> enregistrementMapper.toDto(enregistrement))
                .collect(Collectors.toList());

        // Convertir la liste en Page
        return new PageImpl<>(uniqueEnregistrements, pageable, uniqueEnregistrements.size());
    }

    public Page<EnregistrementDto> findAllPeriodeAndStatut(LocalDate startDate, LocalDate endDate, Long aeroportId,
            List<StatutVoyageur> statuts, Pageable pageable) {
        Page<Enregistrement> enregistrements = enregistrementRepository.findByFilters(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59),
                aeroportId,
                statuts,
                pageable);

       
        // Grouper par numeroDocument et compter les occurrences
        Map<String, List<EnregistrementDto>> groupedByDocument = enregistrements.getContent()
                .stream()
                .map(enregistrement -> enregistrementMapper.toDto(enregistrement))
                .collect(Collectors.groupingBy(EnregistrementDto::getNumeroDocument));

        // Créer une liste sans doublons avec le nombre de voyages
        List<EnregistrementDto> uniqueEnregistrements = groupedByDocument.entrySet()
                .stream()
                .map(entry -> {
                    EnregistrementDto dto = entry.getValue().get(0); // Prendre le premier élément
                    dto.setNbreVoyage(Long.valueOf(entry.getValue().size())); // Définir le nombre de voyages
                    return dto;
                })
                .collect(Collectors.toList());

        // Convertir la liste en Page
        return new PageImpl<>(uniqueEnregistrements, pageable, uniqueEnregistrements.size());
    }

    /**
     * Remove a Enregistrement by id if exists.
     *
     * @param id removed hotel id.
     */
    public void delete(final long id) {
        enregistrementRepository.findById(id).ifPresentOrElse(enregistrement -> {
            enregistrement.setDeleted(Boolean.TRUE);
            enregistrementRepository.save(enregistrement);
            traceService.writeAuditEvent(EntityAuditAction.VOL, ObjetEntity.VOL);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Cannot remove Enregistrement with ID : %d", id));
        });
    }

    public Boolean isExisteByNom(final String nom) {
        Boolean isExiste = villeRepository.existsByDeletedFalseAndNomIgnoreCase(nom);

        return isExiste;
    }

    public PieceJointe uploadFileMultiple(MultipartFile file, InformationPersonnelle enregistrement, int i,
            PieceJointe jointe) {

        try {
            String name = "";

            name = file.getOriginalFilename();
            int r = name.lastIndexOf('.');
            String base = (r == -1) ? name : name.substring(0, r);
            String finalFilename = uploadFile(file, enregistrement.getId().toString());

            if (finalFilename == null) {
                throw new IOException("Échec de l'upload du fichier: " + name);
            }

            if (i == 1) {
                jointe.setCheminImgRecto(finalFilename);
            }

            if (i == 2) {
                jointe.setCheminImgVerso(finalFilename);
            }
            if (i == 3) {
                jointe.setCheminPhotoProfil(finalFilename);
            }

            return jointe;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "fail to upload file: " + e.getMessage());
        }
    }

    private String uploadFile(MultipartFile file, String courrierId) throws IOException {
        if (file.isEmpty() || courrierId == null || courrierId.trim().isEmpty()) {
            return null;
        }

        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return null;
        }

        // Sécuriser le nom de fichier
        String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_àáâãäçèéêëìíîïñòóôõöùúûüýÿ ]", "_");
        String finalFilename = courrierId + sanitizedFilename;
        Path path = Paths.get(uploadDirectory, finalFilename);

        try {
            Files.write(path, file.getBytes());
            System.out.println("Fichier sauvé: " + finalFilename);
            return finalFilename; // Retourner le nom final pour la base de données
        } catch (IOException e) {
            System.err.println("Erreur lors de l'upload: " + e.getMessage());
            e.printStackTrace();
            throw e; // Relancer l'exception pour la gestion d'erreur
        }
    }

}

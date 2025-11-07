package aeroport.bf.service;

import jakarta.transaction.Transactional;
import java_cup.runtime.lr_parser;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.config.security.SecurityUtils;
import aeroport.bf.domain.Ville;
import aeroport.bf.domain.Voyage;
import aeroport.bf.domain.enums.EtatVoyage;
import aeroport.bf.domain.enums.MotifVoyage;
import aeroport.bf.domain.enums.Statut;
import aeroport.bf.domain.enums.StatutVoyage;
import aeroport.bf.domain.enums.StatutVoyageur;
import aeroport.bf.domain.enums.TypeDocument;
import aeroport.bf.domain.Enregistrement;
import aeroport.bf.domain.InformationPersonnelle;
import aeroport.bf.domain.User;
import aeroport.bf.dto.MenuActionDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.dto.EnregistrementDto;
import aeroport.bf.dto.mapper.VilleMapper;
import aeroport.bf.dto.mapper.EnregistrementMapper;
import aeroport.bf.repository.VilleRepository;
import aeroport.bf.repository.VolRepository;
import aeroport.bf.repository.VoyageRepository;
import aeroport.bf.repository.EnregistrementRepository;
import aeroport.bf.repository.InformationPersonnelleRepository;
import aeroport.bf.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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


public EnregistrementDto create(final EnregistrementDto dto) {

    // === 1. Créer et enregistrer InformationPersonnelle ===
    InformationPersonnelle infoPerso = new InformationPersonnelle();
    infoPerso.setNomFamille(dto.getNomFamille());
    infoPerso.setPrenom(dto.getPrenom());
    infoPerso.setDateNaissance(dto.getDateNaissance());
    infoPerso.setLieuNaissance(dto.getLieuNaissance());
    infoPerso.setNationalite(dto.getNationalite());
    infoPerso.setTypeDocument(TypeDocument.CNI);
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
    voyage.setVol(volRepository.getReferenceById(dto.getVolId())); // Assurez-vous que le vol existe
    voyage.setVilleDepart(villeRepository.getReferenceById(1L));
    voyage.setVilleDestination(villeRepository.getReferenceById(2L));
    voyage.setMotifVoyage(MotifVoyage.AFFAIRES);
    voyage.setDateVoyage(dto.getDateVoyage());
    voyage.setHeureVoyage(LocalTime.now());
    voyage.setEtatVoyage(EtatVoyage.ALLER);
    voyage.setDureeSejour(dto.getDureeSejour());
    voyage.setStatut(StatutVoyage.ACTIF);
System.out.println("DTO Nom savedVoyage: " + voyage);
    Voyage savedVoyage = voyageRepository.save(voyage);
System.out.println("DTO Nom savedVoyage: " + savedVoyage);
    // === 3. Créer Enregistrement ===
    Enregistrement enregistrement = new Enregistrement();
     String username = SecurityUtils.getCurrentUsername();
    Optional<User> user= userRepository.findByDeletedFalseAndUsername(username);
    enregistrement.setUtilisateur(user.get());
    enregistrement.setInformationPersonnel(savedInfoPerso);
    enregistrement.setVoyage(savedVoyage);
    enregistrement.setAdresseEtranger(dto.getAdresseEtranger());
    enregistrement.setTelephoneEtranger(dto.getTelephoneEtranger());
    enregistrement.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutVoyageur.EN_ATTENTE);
    enregistrement.setDateSaisie(LocalDate.now());

    // === 4. Sauvegarder Enregistrement ===
    Enregistrement savedEnregistrement = enregistrementRepository.save(enregistrement);
System.out.println("DTO Nom savedEnregistrement: " + savedEnregistrement);
    // === 5. Retourner le DTO ===
    return dto;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created Enregistrement cannot have null ID.");
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

    public Page<EnregistrementDto> findAllPeriodeAndStatut(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Enregistrement> enregistrements = enregistrementRepository.findByDeletedFalseAndDateSaisieBetween(
                startDate,
                endDate,
                pageable);
        return enregistrements.map(enregistrement -> enregistrementMapper.toDto(enregistrement));
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

}

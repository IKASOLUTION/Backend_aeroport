package aeroport.bf.service;

import aeroport.bf.service.util.CurrentUserAeropert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.DonneeBiometrique;
import aeroport.bf.domain.Enregistrement;
import aeroport.bf.domain.InformationPersonnelle;
import aeroport.bf.domain.enums.StatutDonneeBio;
import aeroport.bf.domain.enums.StatutVoyageur;
import aeroport.bf.domain.enums.TypeCapture;
import aeroport.bf.dto.DonneeBiometriqueDto;
import aeroport.bf.dto.EnregistrementDto;
import aeroport.bf.dto.InformationPersonnelleDto;
import aeroport.bf.dto.mapper.DonneeBiometriqueMapper;
import aeroport.bf.dto.mapper.InformationPersonnelleMapper;
import aeroport.bf.repository.DonneeBiometriqueRepository;
import aeroport.bf.repository.InformationPersonnelleRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DonneeBiometriqueService {
    private final DonneeBiometriqueRepository donneeBiometriqueRepository;
    private final DonneeBiometriqueMapper mapper;
    private final TraceService traceService;
    @Value("${files.upload.baseDir}/identite/biometric")
    private String uploadDirectory;
    private final InformationPersonnelleRepository informationPersonnelleRepository;
    private final InformationPersonnelleMapper informationPersonnelleMapper;

    /**
     * Save ticket.
     *
     * @param filialeDto {@link aeroport.bf.dto.FilialeDto}
     * @return saved ticket object
     */
    private DonneeBiometriqueDto saveDonneeBiometrique(final DonneeBiometriqueDto dto) {
            DonneeBiometrique biometrique= mapper.toEntity(dto);
            biometrique.setStatut(StatutDonneeBio.VALIDE);
            biometrique.setDateCapture(LocalDate.now());
            if(dto.getTypeCapture() == null) {
                biometrique.setTypeCapture(TypeCapture.ENROLEMENT);
            }
            biometrique.setAeroport(CurrentUserAeropert.retrieveAeropert());
            biometrique =donneeBiometriqueRepository.save(biometrique);
        try {
            uploadFile(dto.getPhotoBiometrique(), biometrique);
            biometrique.setPhotoBiometriquePath(uploadFile(dto.getPhotoBiometrique(), biometrique));
            biometrique.setEmpreinteDroitePath(uploadFile(dto.getEmpreinteDroite(), biometrique));
            biometrique.setEmpreinteGauchePath(uploadFile(dto.getEmpreinteGauche(), biometrique));
            biometrique.setEmpreintePoucesPath(uploadFile(dto.getEmpreintePouces(), biometrique));
           donneeBiometriqueRepository.save(biometrique);
        } catch (IOException e) {
            
            e.printStackTrace();
        }
       

        dto.setPhotoBiometrique(null);
        dto.setEmpreinteDroite(null);
        dto.setEmpreinteGauche(null);
        dto.setEmpreintePouces(null);


        return dto;
    }

    /**
     * Create new ticket.
     *
     * @param ticketDto {@link aeroport.bf.dto.TicketDto}
     * @return created ticket object
     */
    public DonneeBiometriqueDto createDonneeBiometrique(final DonneeBiometriqueDto dto) {
        return saveDonneeBiometrique(dto);
    }


    private String uploadFile(MultipartFile file, DonneeBiometrique biometrique) throws IOException {
        if (file.isEmpty() || biometrique == null) {
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
        String finalFilename = biometrique.getId() + sanitizedFilename;
        //biometrique.setPhotoBiometriquePath(finalFilename);
        // donneeBiometriqueRepository.save(biometrique);
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

    /**
     * Update existing ticket.
     *
     * @param ticketDto {@link aeroport.bf.dto.TicketDto}
     * @return updated ticket object
     */
    public DonneeBiometriqueDto update(final DonneeBiometriqueDto dto, final long id) {
        return saveDonneeBiometrique(dto);
    }
    public List<DonneeBiometriqueDto> deleteAll(List<DonneeBiometriqueDto> dtos) {
        mapper.toEntity(dtos).stream().peek(filiale -> {
            filiale.setDeleted(Boolean.TRUE);
            donneeBiometriqueRepository.save(filiale);

        }).collect(Collectors.toList());
        return dtos;
    }

    /**
     * Get ticket by id.
     *
     * @param id searched ticket id
     * @return found ticket object
     */
    public DonneeBiometriqueDto findDonneeBiometrique(final long id) {
        if (!donneeBiometriqueRepository.existsByDeletedFalseAndId(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No filiale exists with this ID : %d", id));
        }
        return mapper.toDto(donneeBiometriqueRepository.findOneByDeletedFalseAndId(id));
    }

    /**
     * Fetch all ticket stored in DB.
     *
     * @return list of {@link aeroport.bf.dto.TicketDto}
     */
    public List<DonneeBiometriqueDto> findAllDonneeBiometrique() {
        List<DonneeBiometrique> dts = donneeBiometriqueRepository.findAllByDeletedFalse(
                CurrentUserAeropert.retrieveAeropert().getId());
        if (dts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,
                    "No data found, Please create at least one filiale before.");
        }
        return mapper.toDto(dts);
    }

    public List<InformationPersonnelleDto> findAllPersonne(){
        return informationPersonnelleMapper.toDto(informationPersonnelleRepository.findAllByDeletedFalse());
    }

    /**
     * Remove a ticket by id if exists.
     *
     * @param id removed ticket id.
     */
    public void delete(final long id) {
        donneeBiometriqueRepository.findTop1ByDeletedFalseAndId(id).ifPresentOrElse(filiale -> {
            filiale.setDeleted(Boolean.TRUE);
            donneeBiometriqueRepository.save(filiale);
            traceService.writeAuditEvent(EntityAuditAction.DELETE, ObjetEntity.PROFIL);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Cannot remove filiale with ID : %d", id));
        });
    }



    public Page<DonneeBiometriqueDto> findAllPeriodeAndStatut(LocalDate startDate, LocalDate endDate,
           Pageable pageable) {
        Page<DonneeBiometrique> donnneBiometric = donneeBiometriqueRepository.findByFilters(
        startDate,
        endDate.plusDays(1),
        CurrentUserAeropert.retrieveAeropert().getId(),
        pageable
);

       
       

       
        List<DonneeBiometriqueDto> uniques = donnneBiometric.getContent()
                .stream()
               .map(donnee -> mapper.toDto(donnee))
                .collect(Collectors.toList());
       

        // Convertir la liste en Page
        return new PageImpl<>(uniques, pageable, uniques.size());
    }

}

package aeroport.bf.service;

import aeroport.bf.service.util.CurrentUserAeropert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.DonneeBiometrique;
import aeroport.bf.dto.DonneeBiometriqueDto;
import aeroport.bf.dto.mapper.DonneeBiometriqueMapper;
import aeroport.bf.repository.DonneeBiometriqueRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

    /**
     * Save ticket.
     *
     * @param filialeDto {@link aeroport.bf.dto.FilialeDto}
     * @return saved ticket object
     */
    private DonneeBiometriqueDto saveDonneeBiometrique(final DonneeBiometriqueDto dto) {
            DonneeBiometrique biometrique= mapper.toEntity(dto);
            biometrique.setAeroport(CurrentUserAeropert.retrieveAeropert());
            biometrique =donneeBiometriqueRepository.save(biometrique);
        try {
            uploadFile(dto.getPhotoBiometrique(), biometrique);
        } catch (IOException e) {
            
            e.printStackTrace();
        }
       


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
        biometrique.setPhotoBiometriquePath(finalFilename);
        donneeBiometriqueRepository.save(biometrique);
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

}

package aeroport.bf.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.Ville;
import aeroport.bf.domain.Enregistrement;
import aeroport.bf.dto.MenuActionDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.dto.EnregistrementDto;
import aeroport.bf.dto.mapper.VilleMapper;
import aeroport.bf.dto.mapper.EnregistrementMapper;
import aeroport.bf.repository.VilleRepository;
import aeroport.bf.repository.EnregistrementRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EnregistrementService {
    private final EnregistrementRepository volRepository;
    private final EnregistrementMapper volMapper;
    private final TraceService traceService;
    private final VilleRepository villeRepository;

    /**
     * Save compagine.
     *
     * @param dto {@link EnregistrementDto}
     * @return saved Enregistrement object
     */
    public EnregistrementDto create(final EnregistrementDto dto) {

        Enregistrement vol = volMapper.toEntity(dto);
        
        return volMapper.toDto(volRepository.save(vol));
    }

    /**
     * Update existing Enregistrement.
     *
     * @param dto {@link EnregistrementDto}
     * @param id
     * @return updated Enregistrement object
     */
    public EnregistrementDto update(final EnregistrementDto dto, final long id) {

        if (!volRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No Enregistrement exists with this ID : %d", id));
        }
        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created Enregistrement cannot have null ID.");
        }
        Enregistrement vol = volMapper.toEntity(dto);
        
        return volMapper.toDto(volRepository.save(vol));
    }

    /**
     * Get Enregistrement by id.
     *
     * @param id searched Hotel id
     * @return found EnregistrementDto object
     */
    public EnregistrementDto findOne(final long id) {
        if (!volRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No Enregistrement exists with this ID : %d", id));
        }
        return volMapper.toDto(Objects.requireNonNull(volRepository.findById(id).orElse(null)));
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
        return volMapper.toDto(volRepository.findAllByDeletedFalse());

    }

    public Page<EnregistrementDto> findAllPeriodeAndStatut(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Enregistrement> vols = volRepository.findByDeletedFalseAndDateSaisieBetween(
                startDate,
                endDate,
                pageable);
        return vols.map(vol -> volMapper.toDto(vol));
    }

    /**
     * Remove a Enregistrement by id if exists.
     *
     * @param id removed hotel id.
     */
    public void delete(final long id) {
        volRepository.findById(id).ifPresentOrElse(vol -> {
            vol.setDeleted(Boolean.TRUE);
            volRepository.save(vol);
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

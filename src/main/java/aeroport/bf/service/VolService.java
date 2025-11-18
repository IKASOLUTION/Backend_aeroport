package aeroport.bf.service;

import aeroport.bf.service.util.CurrentUserAeropert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.jfree.util.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.Ville;
import aeroport.bf.domain.Vol;
import aeroport.bf.domain.enums.StatutVol;
import aeroport.bf.dto.MenuActionDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.dto.VolDto;
import aeroport.bf.dto.mapper.VilleMapper;
import aeroport.bf.dto.mapper.VolMapper;
import aeroport.bf.repository.AeroportRepository;
import aeroport.bf.repository.CompagnieRepository;
import aeroport.bf.repository.VilleRepository;
import aeroport.bf.repository.VolRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class VolService {
    private final VolRepository volRepository;
    private final VolMapper volMapper;
    private final TraceService traceService;
    private final VilleRepository villeRepository;

    /**
     * Save compagine.
     *
     * @param dto {@link VolDto}
     * @return saved Vol object
     */
    public VolDto create(final VolDto dto) {

        Vol vol = volMapper.toEntity(dto);
        vol.setAeroport(CurrentUserAeropert.retrieveAeropert());
        return volMapper.toDto(volRepository.save(vol));
    }

    /**
     * Update existing Vol.
     *
     * @param dto {@link VolDto}
     * @param id
     * @return updated Vol object
     */
    public VolDto update(final VolDto dto, final long id) {

        if (!volRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No Vol exists with this ID : %d", id));
        }
        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created Vol cannot have null ID.");
        }
          // Convertir DTO → Entity
     Vol vol = volMapper.toEntity(dto);        
        return volMapper.toDto(volRepository.saveAndFlush(vol));
    }

    /**
     * Get Vol by id.
     *
     * @param id searched Hotel id
     * @return found VolDto object
     */
    public VolDto findOne(final long id) {
        if (!volRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No Vol exists with this ID : %d", id));
        }
        return volMapper.toDto(Objects.requireNonNull(volRepository.findById(id).orElse(null)));
    }

    /**
     * Fetch page Vol stored in DB.
     * 
     * @param dto of {@link VolDto}
     * @return page of {@link VolDto}
     */

    /**
     * Fetch all Vol stored in DB.
     * 
     * @return list of {@link VolDto}
     */
    public List<VolDto> findAll() {
        return volMapper.toDto(volRepository.findAllByDeletedFalse());

    }

    public Page<VolDto> findAllPeriodeAndStatut(LocalDate startDate, LocalDate endDate,List<StatutVol> statuts, Pageable pageable) {
        System.out.println("Received statuts request: " + statuts);
        if (statuts.isEmpty()) {
            statuts = null;
        }
        Page<Vol> vols = volRepository.findByDeletedFalseAndStatutInAndDateDepartBetween(
        statuts,        
        startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay(),
                CurrentUserAeropert.retrieveAeropert().getId(),
                pageable);
        return vols.map(volMapper::toDto);
    }

    /**
     * Remove a Vol by id if exists.
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
                    String.format("Cannot remove Vol with ID : %d", id));
        });
    }

    public Boolean isExisteByNom(final String nom) {
        Boolean isExiste = villeRepository.existsByDeletedFalseAndNomIgnoreCase(nom);

        return isExiste;
    }

}

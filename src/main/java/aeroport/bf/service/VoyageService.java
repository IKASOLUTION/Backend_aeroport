package aeroport.bf.service;

import aeroport.bf.service.util.CurrentUserAeropert;
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
import aeroport.bf.domain.Voyage;
import aeroport.bf.domain.enums.StatutVoyage;
import aeroport.bf.dto.MenuActionDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.dto.VoyageDto;
import aeroport.bf.dto.mapper.VilleMapper;
import aeroport.bf.dto.mapper.VoyageMapper;
import aeroport.bf.repository.VilleRepository;
import aeroport.bf.repository.VoyageRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class VoyageService {
    private final VoyageRepository volRepository;
    private final VoyageMapper volMapper;
    private final TraceService traceService;
    private final VilleRepository villeRepository;

    /**
     * Save compagine.
     *
     * @param dto {@link VoyageDto}
     * @return saved Voyage object
     */
    public VoyageDto create(final VoyageDto dto) {

        Voyage vol = volMapper.toEntity(dto);
        vol.setAeroport(CurrentUserAeropert.retrieveAeropert());
       /*  if (dto.getVilleNomA() != null) {
            if (!isExisteByNom(dto.getVilleNomA())) {
                Ville villeA = new Ville();
                villeA.setNom(dto.getVilleNomA());
                villeA = villeRepository.save(villeA);
                vol.setVilleDestination(villeA);
            } else {
                villeRepository.findByNomIgnoreCaseAndDeletedFalse(dto.getVilleNomA())
                        .ifPresent(ville -> vol.setVilleDestination(ville));
 
            }

        }
            
        if (dto.getVilleNomD() != null) {
            if (!isExisteByNom(dto.getVilleNomD())) {
                Ville villeD = new Ville();
                villeD.setNom(dto.getVilleNomD());
                villeD = villeRepository.save(villeD);
                vol.setVilleDepart(villeD);
            } else {
                villeRepository.findByNomIgnoreCaseAndDeletedFalse(dto.getVilleNomA())
                        .ifPresent(ville -> vol.setVilleDepart(ville));

            }
        }*/
        return volMapper.toDto(volRepository.save(vol));
    }

    /**
     * Update existing Voyage.
     *
     * @param dto {@link VoyageDto}
     * @param id
     * @return updated Voyage object
     */
    public VoyageDto update(final VoyageDto dto, final long id) {

        if (!volRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No Voyage exists with this ID : %d", id));
        }
        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created Voyage cannot have null ID.");
        }
        Voyage vol = volMapper.toEntity(dto);
       /*  if (dto.getVilleNomA() != null) {
            if (!isExisteByNom(dto.getVilleNomA())) {
                Ville villeA = new Ville();
                villeA.setNom(dto.getVilleNomA());
                villeA = villeRepository.save(villeA);
                vol.setVilleDestination(villeA);
            } else {
                villeRepository.findByNomIgnoreCaseAndDeletedFalse(dto.getVilleNomA())
                        .ifPresent(ville -> vol.setVilleDestination(ville));
            }

        }
        if (dto.getVilleNomD() != null) {
            if (!isExisteByNom(dto.getVilleNomD())) {
                Ville villeD = new Ville();
                villeD.setNom(dto.getVilleNomD());
                villeD = villeRepository.save(villeD);
                vol.setVilleDepart(villeD);
            } else {
                villeRepository.findByNomIgnoreCaseAndDeletedFalse(dto.getVilleNomA())
                        .ifPresent(ville -> vol.setVilleDepart(ville));

            }

        }
 */        return volMapper.toDto(volRepository.save(vol));
    }

    /**
     * Get Voyage by id.
     *
     * @param id searched Hotel id
     * @return found VoyageDto object
     */
    public VoyageDto findOne(final long id) {
        if (!volRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No Voyage exists with this ID : %d", id));
        }
        return volMapper.toDto(Objects.requireNonNull(volRepository.findById(id).orElse(null)));
    }

    /**
     * Fetch page Voyage stored in DB.
     * 
     * @param dto of {@link VoyageDto}
     * @return page of {@link VoyageDto}
     */

    /**
     * Fetch all Voyage stored in DB.
     * 
     * @return list of {@link VoyageDto}
     */
    public List<VoyageDto> findAll() {
        return volMapper.toDto(volRepository.findAllByDeletedFalse());

    }

    public Page<VoyageDto> findAllPeriodeAndStatut(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Voyage> vols = volRepository.findByDeletedFalseAndDateVoyageBetween(
                startDate,
                endDate,
                CurrentUserAeropert.retrieveAeropert().getId(),
                pageable);
        return vols.map(vol -> volMapper.toDto(vol));
    }

    /**
     * Remove a Voyage by id if exists.
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
                    String.format("Cannot remove Voyage with ID : %d", id));
        });
    }

    public Boolean isExisteByNom(final String nom) {
        Boolean isExiste = villeRepository.existsByDeletedFalseAndNomIgnoreCase(nom);

        return isExiste;
    }

}

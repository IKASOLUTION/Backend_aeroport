package aeroport.bf.service;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.MotifVoyage;
import aeroport.bf.dto.MotifVoyageDto;
import aeroport.bf.dto.mapper.MotifVoyageMapper;
import aeroport.bf.repository.MotifVoyageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MotifVoyageService {
    private final MotifVoyageRepository motifVoyageRepository;
    private final MotifVoyageMapper motifVoyageMapper;
    private final TraceService traceService;


    /**
     * Save motif.
     *
     * @param dto {@link MotifVoyageDto}
     * @return saved MotifVoyage object
     */
    public MotifVoyageDto create(final MotifVoyageDto dto) {
        MotifVoyage motif = motifVoyageMapper.toEntity(dto);
        motif= motifVoyageRepository.save(motif);
        return motifVoyageMapper.toDto(motif);
    }

    /**
     * Update existing MotifVoyage.
     *
     * @param dto {@link MotifVoyageDto}
     * @return updated MotifVoyage object
     */
    public MotifVoyageDto update(final MotifVoyageDto dto, final long id) {
        if (!motifVoyageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No motif exists with this ID : %d", id));
        }

        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created motif cannot have null ID.");
        }
        MotifVoyage motif = motifVoyageMapper.toEntity(dto);
        return motifVoyageMapper.toDto(motifVoyageRepository.save(motif));
    }

    /**
     * Get MotifVoyage by id.
     *
     * @param id searched ticket id
     * @return found MotifVoyage object
     */
    public MotifVoyageDto findOne(final long id) {
        if (!motifVoyageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No lotif exists with this ID : %d", id));
        }
        return motifVoyageMapper.toDto(Objects.requireNonNull(motifVoyageRepository.findById(id).orElse(null)));
    }

    /**
     * Fetch all MotifVoyage stored in DB.
     * @return list of {@link MotifVoyageDto}
     */
    public List<MotifVoyageDto> findAll() {
        return motifVoyageRepository.findAllByDeletedIsFalse().stream().map(motifVoyageMapper::toDto).toList();

    }

    /**
     * Remove a MotifVoyage by id if exists.
     *
     * @param id removed MotifVoyage id.
     */
    public void delete(final long id) {
        motifVoyageRepository.findById(id).ifPresentOrElse(motif -> {
            motif.setDeleted(Boolean.TRUE);
            motifVoyageRepository.save(motif);
            traceService.writeAuditEvent( EntityAuditAction.DELETE, ObjetEntity.MOTIF_VOYAGE);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove motif with ID : %d", id));
        });
    }

}

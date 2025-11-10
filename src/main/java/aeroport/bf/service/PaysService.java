package aeroport.bf.service;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.Pays;
import aeroport.bf.dto.PaysDto;
import aeroport.bf.dto.mapper.PaysMapper;
import aeroport.bf.repository.PaysRepository;
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
public class PaysService {
    private final PaysRepository paysRepository;
    private final PaysMapper paysMapper;
    private final TraceService traceService;


    /**
     * Save pays.
     *
     * @param dto {@link PaysDto}
     * @return saved Pays object
     */
    public PaysDto create(final PaysDto dto) {
        System.out.println("addddd" + dto);
        
        Pays pays = paysMapper.toEntity(dto);
        pays= paysRepository.save(pays);
        return paysMapper.toDto(pays);
    }

    /**
     * Update existing pays.
     *
     * @param dto {@link PaysDto}
     * @return updated region object
     */
    public PaysDto update(final PaysDto dto, final long id) {
        if (!paysRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No pays exists with this ID : %d", id));
        }

        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created pays cannot have null ID.");
        }
        Pays region = paysMapper.toEntity(dto);
        return paysMapper.toDto(paysRepository.save(region));
    }

    /**
     * Get Pays by id.
     *
     * @param id searched ticket id
     * @return found Pays object
     */
    public PaysDto findOne(final long id) {
        if (!paysRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No pays exists with this ID : %d", id));
        }
        return paysMapper.toDto(paysRepository.findById(id).orElse(null));
    }

    /**
     * Fetch all Pays stored in DB.
     * @return list of {@link PaysDto}
     */
    public List<PaysDto> findAll() {
        return paysRepository.findAllByDeletedIsFalse().stream().map(paysMapper::toDto).toList();

    }

    /**
     * Remove a Pays by id if exists.
     *
     * @param id removed pays id.
     */
    public void delete(final long id) {
        paysRepository.findById(id).ifPresentOrElse(pays -> {
            pays.setDeleted(Boolean.TRUE);
            paysRepository.save(pays);
            traceService.writeAuditEvent( EntityAuditAction.DELETE, ObjetEntity.PAYS);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove pays with ID : %d", id));
        });
    }

}

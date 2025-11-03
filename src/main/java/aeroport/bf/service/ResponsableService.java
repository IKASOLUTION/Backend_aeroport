package aeroport.bf.service;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.Responsable;
import aeroport.bf.dto.ResponsableDto;
import aeroport.bf.dto.mapper.ResponsableMapper;
import aeroport.bf.repository.ResponsableRepository;
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
public class ResponsableService {
    private final ResponsableRepository responsableRepository;
    private final ResponsableMapper responsableMapper;
    private final TraceService traceService;


    /**
     * Save responsable.
     *
     * @param dto {@link ResponsableDto}
     * @return saved Pays object
     */
    public ResponsableDto create(final ResponsableDto dto) {
        Responsable responsable = responsableMapper.toEntity(dto);
        responsable= responsableRepository.save(responsable);
        return responsableMapper.toDto(responsable);
    }

    /**
     * Update existing responsable.
     *
     * @param dto {@link ResponsableDto}
     * @return updated region object
     */
    public ResponsableDto update(final ResponsableDto dto, final long id) {
        if (!responsableRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No pays exists with this ID : %d", id));
        }

        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created pays cannot have null ID.");
        }
        Responsable responsable = responsableMapper.toEntity(dto);
        return responsableMapper.toDto(responsableRepository.save(responsable));
    }

    /**
     * Get Responsable by id.
     *
     * @param id searched ticket id
     * @return found Responsable object
     */
    public ResponsableDto findOne(final long id) {
        if (!responsableRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No pays exists with this ID : %d", id));
        }
        return responsableMapper.toDto(responsableRepository.findById(id).orElse(null));
    }

    /**
     * Fetch all Responsable stored in DB.
     * @return list of {@link ResponsableDto}
     */
    public List<ResponsableDto> findAll() {
        return responsableRepository.findAll().stream().map(responsableMapper::toDto).toList();

    }

    /**
     * Remove a responsable by id if exists.
     *
     * @param id removed pays id.
     */
    public void delete(final long id) {
        responsableRepository.findById(id).ifPresentOrElse(resp -> {
            resp.setDeleted(Boolean.TRUE);
            responsableRepository.save(resp);
            traceService.writeAuditEvent( EntityAuditAction.DELETE, ObjetEntity.RESPONSABLE);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove responsable with ID : %d", id));
        });
    }

}

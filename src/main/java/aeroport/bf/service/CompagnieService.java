package aeroport.bf.service;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.Compagnie;
import aeroport.bf.domain.enums.Statut;
import aeroport.bf.dto.CompagnieDto;
import aeroport.bf.dto.mapper.CompagnieMapper;
import aeroport.bf.repository.CompagnieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CompagnieService {
    private final CompagnieRepository compagnieRepository;
    private final CompagnieMapper compagnieMapper;
    private final TraceService traceService;

    /**
     * Save compagine.
     *
     * @param dto {@link CompagnieDto}
     * @return saved compagnie object
     */
    public CompagnieDto create(final CompagnieDto dto) {
        Compagnie compagnie = compagnieMapper.toEntity(dto);
        compagnie.setStatut(Statut.ACTIF);
        compagnie= compagnieRepository.save(compagnie);
        return compagnieMapper.toDto(compagnie);
    }

    /**
     * Update existing compagnie.
     *
     * @param dto {@link CompagnieDto}
     * @param id
     * @return updated Compagnie object
     */
    public CompagnieDto update(final CompagnieDto dto, final long id) {

        if (!compagnieRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No compagnie exists with this ID : %d", id));
        }
        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created compagnie cannot have null ID.");
        }
        Compagnie compagnie = compagnieMapper.toEntity(dto);
        return compagnieMapper.toDto(compagnieRepository.save(compagnie));
    }

    /**
     * Get Compagnie by id.
     *
     * @param id searched Hotel id
     * @return found CompagnieDto object
     */
    public CompagnieDto findOne(final long id) {
        if (!compagnieRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No compagnie exists with this ID : %d", id));
        }
        return compagnieMapper.toDto(Objects.requireNonNull(compagnieRepository.findById(id).orElse(null)));
    }

    /**
     * Fetch page compagnie stored in DB.
     * @param dto of {@link CompagnieDto}
     * @return page of {@link CompagnieDto}
     */
    public Page<CompagnieDto> findByPage(CompagnieDto dto, Pageable pageable) {
        return compagnieRepository.findWithCriteria(
                dto.getPays()!=null ? dto.getPays().getId() : null,
                dto.getNomCompagine(),pageable).map(compagnieMapper::toDto);

    }

    /**
     * Fetch all compagnie stored in DB.
     * @return list of {@link CompagnieDto}
     */
    public List<CompagnieDto> findAll() {
        return compagnieRepository.findAllByDeletedIsFalse().stream().map(compagnieMapper::toDto).toList();

    }

    /**
     * Remove a compagnie by id if exists.
     *
     * @param id removed hotel id.
     */
    public void delete(final long id) {
        compagnieRepository.findById(id).ifPresentOrElse(compagnie -> {
            compagnie.setDeleted(Boolean.TRUE);
            compagnieRepository.save(compagnie);
            traceService.writeAuditEvent( EntityAuditAction.DELETE, ObjetEntity.COMPAGNIE);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove compagnie with ID : %d", id));
        });
    }

}

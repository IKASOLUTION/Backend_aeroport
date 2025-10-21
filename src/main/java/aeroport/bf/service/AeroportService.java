package aeroport.bf.service;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.Aeroport;
import aeroport.bf.domain.enums.Statut;
import aeroport.bf.dto.AeroportDto;
import aeroport.bf.dto.mapper.AeroportMapper;
import aeroport.bf.dto.mapper.AeroportMapper;
import aeroport.bf.repository.AeroportRepository;
import aeroport.bf.repository.AeroportRepository;
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
public class AeroportService {
    private final AeroportRepository aeroportRepository;
    private final AeroportMapper aeroportMapper;
    private final TraceService traceService;

    /**
     * Save compagine.
     *
     * @param dto {@link AeroportDto}
     * @return saved Aeroport object
     */
    public AeroportDto create(final AeroportDto dto) {
        Aeroport Aeroport = aeroportMapper.toEntity(dto);
        Aeroport.setStatut(Statut.ACTIF);
        Aeroport= aeroportRepository.save(Aeroport);
        return aeroportMapper.toDto(Aeroport);
    }

    /**
     * Update existing Aeroport.
     *
     * @param dto {@link AeroportDto}
     * @param id
     * @return updated Aeroport object
     */
    public AeroportDto update(final AeroportDto dto, final long id) {

        if (!aeroportRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No Aeroport exists with this ID : %d", id));
        }
        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created Aeroport cannot have null ID.");
        }
        Aeroport Aeroport = aeroportMapper.toEntity(dto);
        return aeroportMapper.toDto(aeroportRepository.save(Aeroport));
    }

    /**
     * Get Aeroport by id.
     *
     * @param id searched Hotel id
     * @return found AeroportDto object
     */
    public AeroportDto findOne(final long id) {
        if (!aeroportRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No Aeroport exists with this ID : %d", id));
        }
        return aeroportMapper.toDto(Objects.requireNonNull(aeroportRepository.findById(id).orElse(null)));
    }

    /**
     * Fetch page Aeroport stored in DB.
     * @param dto of {@link AeroportDto}
     * @return page of {@link AeroportDto}
     */
   

    /**
     * Fetch all Aeroport stored in DB.
     * @return list of {@link AeroportDto}
     */
    public List<AeroportDto> findAll() {
        return aeroportRepository.findAllByDeletedIsFalse().stream().map(aeroportMapper::toDto).toList();

    }

    /**
     * Remove a Aeroport by id if exists.
     *
     * @param id removed hotel id.
     */
    public void delete(final long id) {
        aeroportRepository.findById(id).ifPresentOrElse(aeroport -> {
                aeroport.setDeleted(Boolean.TRUE);
            aeroportRepository.save(aeroport);
            traceService.writeAuditEvent( EntityAuditAction.DELETE, ObjetEntity.Aeroport);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove Aeroport with ID : %d", id));
        });
    }

}

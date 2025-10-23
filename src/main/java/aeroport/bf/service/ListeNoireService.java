package aeroport.bf.service;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.ListeNoire;
import aeroport.bf.dto.ListeNoireDto;
import aeroport.bf.dto.mapper.ListeNoireMapper;
import aeroport.bf.repository.ListeNoireRepository;
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
public class ListeNoireService {
    private final ListeNoireRepository listeNoireRepository;
    private final ListeNoireMapper listeNoireMapper;
    private final TraceService traceService;


    /**
     * Save liste noire.
     *
     * @param dto {@link ListeNoireDto}
     * @return saved liste noire object
     */
    public ListeNoireDto create(final ListeNoireDto dto) {
        ListeNoire liste = listeNoireMapper.toEntity(dto);
        liste= listeNoireRepository.save(liste);
        return listeNoireMapper.toDto(liste);
    }

    /**
     * Update existing liste.
     *
     * @param dto {@link ListeNoireDto}
     * @return updated Liste object
     */
    public ListeNoireDto update(final ListeNoireDto dto, final long id) {

        if (!listeNoireRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No data exists with this ID : %d", id));
        }
        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created liste noir cannot have null ID.");
        }
        ListeNoire liste = listeNoireMapper.toEntity(dto);
        return listeNoireMapper.toDto(listeNoireRepository.save(liste));
    }

    /**
     * Get Liste noire by id.
     *
     * @param id searched liste id
     * @return found ListeNoireDto object
     */
    public ListeNoireDto findOne(final long id) {
        if (!listeNoireRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No data exists with this ID : %d", id));
        }
        return listeNoireMapper.toDto(Objects.requireNonNull(listeNoireRepository.findById(id).orElse(null)));
    }

    /**
     * Fetch page liste stored in DB.
     * @param dto of {@link ListeNoireDto}
     * @return page of {@link ListeNoireDto}
     */
    public Page<ListeNoireDto> findByPage(ListeNoireDto dto, Pageable pageable) {
        return listeNoireRepository.findWithCriteria(
                dto.getNom(), dto.getPrenom(),dto.getLieuNaissance(), dto.getStatut(), dto.getDateNaissance(), pageable).map(listeNoireMapper::toDto);

    }

    /**
     * Fetch all liste nore stored in DB.
     * @return list of {@link ListeNoireDto}
     */
    public List<ListeNoireDto> findAll() {
        return listeNoireRepository.findAllByDeletedIsFalse().stream().map(listeNoireMapper::toDto).toList();

    }

    /**
     * Remove a Liste noire by id if exists.
     *
     * @param id removed liste noire id.
     */
    public void delete(final long id) {
        listeNoireRepository.findById(id).ifPresentOrElse(liste -> {
            liste.setDeleted(Boolean.TRUE);
            listeNoireRepository.save(liste);
            traceService.writeAuditEvent( EntityAuditAction.DELETE, ObjetEntity.LISTE_NOIRE);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove liste noire with ID : %d", id));
        });
    }

}

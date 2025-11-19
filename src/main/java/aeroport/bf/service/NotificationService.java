package aeroport.bf.service;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.ListeNoire;
import aeroport.bf.domain.Notification;
import aeroport.bf.domain.enums.Statut;
import aeroport.bf.dto.ListeNoireDto;
import aeroport.bf.dto.NotificationDto;
import aeroport.bf.dto.mapper.NotificationMapper;
import aeroport.bf.repository.NotificationRepository;
import aeroport.bf.service.util.CurrentUserAeropert;
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
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final TraceService traceService;
    private final ListeNoireService listeNoireService;


    /**
     * Save notification.
     *
     * @param dto {@link NotificationDto}
     * @return saved Pays object
     */
    public NotificationDto create(final NotificationDto dto) {
        ListeNoireDto listeNoire = listeNoireService.findPersonneExisteListeNoire(dto.getNom(), dto.getPrenom(), dto.getNumeroNip());
        if (listeNoire != null) {
            Notification notification = notificationMapper.toEntity(dto);
            notification.setStatut(Statut.ACTIF);
            notification.setAeroport(CurrentUserAeropert.retrieveAeropert());
            notification= notificationRepository.save(notification);
            return notificationMapper.toDto(notification);
        }
        return null;
    }

    /**
     * Update existing Notification.
     *
     * @param dto {@link NotificationDto}
     * @return updated region object
     */
    public NotificationDto update(final NotificationDto dto, final long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No notification exists with this ID : %d", id));
        }

        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created notification cannot have null ID.");
        }
        Notification notification = notificationMapper.toEntity(dto);
        return notificationMapper.toDto(notificationRepository.save(notification));
    }

    /**
     * Get Notification by id.
     *
     * @param id searched ticket id
     * @return found Pays object
     */
    public NotificationDto findOne(final long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No notification exists with this ID : %d", id));
        }
        return notificationMapper.toDto(notificationRepository.findById(id).orElse(null));
    }

    /**
     * Fetch all Notifications stored in DB.
     * @return list of {@link NotificationDto}
     */
    public List<NotificationDto> findAll() {
        return notificationRepository.findAllByDeletedIsFalse().stream().map(notificationMapper::toDto).toList();

    }

    /**
     * Remove a notification by id if exists.
     *
     * @param id removed pays id.
     */
    public void delete(final long id) {
        notificationRepository.findById(id).ifPresentOrElse(notification -> {
            notification.setDeleted(Boolean.TRUE);
            notificationRepository.save(notification);
            traceService.writeAuditEvent( EntityAuditAction.DELETE, ObjetEntity.NOTIFICATION);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove notification with ID : %d", id));
        });
    }

}

package aeroport.bf.repository;

import aeroport.bf.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByDeletedIsFalse();

    Page<Notification> findAllByDeletedIsFalse(Pageable pageable);
}

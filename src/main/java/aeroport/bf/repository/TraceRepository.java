package aeroport.bf.repository;

import aeroport.bf.domain.Trace;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface TraceRepository extends JpaRepository<Trace, Long> {
    List<Trace> findByDateSaisieBetweenAndDeletedFalse(LocalDateTime dateD,LocalDateTime dateF);
    List<Trace> findAllByDeletedFalse();
}

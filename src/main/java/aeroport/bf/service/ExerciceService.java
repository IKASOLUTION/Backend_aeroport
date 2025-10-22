package aeroport.bf.service;
import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.Exercice;
import aeroport.bf.dto.ExerciceDto;
import aeroport.bf.dto.mapper.ExerciceMapper;
import aeroport.bf.repository.ExerciceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciceService {
    private final ExerciceRepository exerciceRepository;
    private final ExerciceMapper exerciceMapper;
    private final TraceService traceService;


    private ExerciceDto saveExercice(final ExerciceDto exerciceDto) {
       
         exerciceRepository.save(exerciceMapper.toEntity(exerciceDto));
        
        return exerciceDto;
    }

    /**
     * Create new ticket.
     *
     * @param ticketDto {@link test.projects.ennov.dto.TicketDto}
     * @return created ticket object
     */
    public ExerciceDto createExercice(final ExerciceDto dto) {
        if (exerciceRepository.existsByDeletedFalseAndAnnee(dto.getAnnee())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "exercice already exists.");
        }
        if (isExisteByCode(dto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code existe déjà.");
        }

        dto.setIsDeleted(Boolean.FALSE);
        dto.setActive(Boolean.FALSE);
        return saveExercice(dto);
    }

    /**
     * Update existing ticket.
     *
     * @param ticketDto {@link test.projects.ennov.dto.TicketDto}
     * @return updated ticket object
     */
    public ExerciceDto updateExercice(final ExerciceDto dto, final long id) {
        if (!exerciceRepository.existsByDeletedFalseAndId(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No exercice exists with this ID : %d", id));
        }

        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created exercice cannot have null ID.");
        }

       /*  if (exerciceRepository.existsByDeletedFalseAndNomAndIdNot(dto.getNom(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A exercice with the same title already exists.");
        } */

    if (isExisteByCode(dto)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code de  existe déjà.");
    }
        return saveExercice(dto);
    }

    public ExerciceDto ActiveExercice(final ExerciceDto dto, final long id) {
        if (!exerciceRepository.existsByDeletedFalseAndId(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No exercice exists with this ID : %d", id));
        }

        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created exercice cannot have null ID.");
        }

       /*  if (exerciceRepository.existsByDeletedFalseAndNomAndIdNot(dto.getNom(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A exercice with the same title already exists.");
        } */

    if (isExisteByCode(dto)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le code de  existe déjà.");
    }
    if(dto.getActive()) {
        dto.setActive(Boolean.FALSE);
    } else {
        Optional<Exercice> exercice = exerciceRepository.findByActiveTrue();
        if(exercice.isPresent()) {
            exercice.get().setActive(Boolean.FALSE);
            exerciceRepository.save(exercice.get());
        }
        dto.setActive(Boolean.TRUE);
    }
        return saveExercice(dto);
    }


    public Boolean isExisteByCode(final ExerciceDto exerciceDto) {
        
        Boolean isExiste = false;
        if(exerciceDto.getId() !=null) {
            if(!exerciceRepository.findAllByDeletedFalseAndAnnee(exerciceDto.getAnnee()).isEmpty() 
            && exerciceRepository.findAllByDeletedFalseAndAnnee(exerciceDto.getAnnee()).size() > 1) {
                isExiste = true;               
                } 
        } else {
             isExiste = exerciceRepository.existsByDeletedFalseAndAnnee(exerciceDto.getAnnee());

        }

        if (isExiste.equals(Boolean.TRUE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet annee existe déjà");
            }

        return isExiste;
    }



     public ExerciceDto findExercice(final long id) {
        if (!exerciceRepository.existsByDeletedFalseAndId(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No exercice exists with this ID : %d", id));
        }

        return exerciceMapper.toDto(exerciceRepository.findOneByDeletedFalseAndId(id));
    }

    /**
     * Fetch all ticket stored in DB.
     *
     * @return list of {@link test.projects.ennov.dto.TicketDto}
     */
    public List<ExerciceDto> findAllExercice() {
        List<Exercice> exercices = exerciceRepository.findAllByDeletedFalse();
        if (exercices.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found, Please create at least one exercice before.");
        }
        return exerciceMapper.toDtos(exercices);
    }

    /**
     * Remove a ticket by id if exists.
     *
     * @param id removed ticket id.
     */
    public void deleteExercice(final long id) {
       
        exerciceRepository.findTop1ByDeletedFalseAndId(id).ifPresentOrElse(exercice -> {
            exercice.setDeleted(Boolean.TRUE);
            exerciceRepository.save(exercice);
            traceService.writeAuditEvent( EntityAuditAction.DELETE, ObjetEntity.EXERCICE);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove exercice with ID : %d", id));
        });
    }

 }
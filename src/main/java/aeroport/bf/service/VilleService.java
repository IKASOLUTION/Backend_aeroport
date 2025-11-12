package aeroport.bf.service;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.Pays;
import aeroport.bf.domain.Ville;
import aeroport.bf.dto.PaysDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.dto.mapper.PaysMapper;
import aeroport.bf.dto.mapper.VilleMapper;
import aeroport.bf.repository.PaysRepository;
import aeroport.bf.repository.VilleRepository;
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
public class VilleService {
    private final VilleRepository villeRepository;
    private final VilleMapper villeMapper;
    private final TraceService traceService;


    /**
     * Save pays.
     *
     * @param dto {@link VilleDto}
     * @return saved Pays object
     */
    public VilleDto create(final VilleDto dto) {
        System.out.println("addddd" + dto);
        
        Ville ville = villeMapper.toEntity(dto);
        ville= villeRepository.save(ville);
        return villeMapper.toDto(ville);
    }

    /**
     * Update existing pays.
     *
     * @param dto {@link VilleDto}
     * @return updated region object
     */
   public VilleDto update(final VilleDto dto, final long id) {
      System.out.println("-------Afficher dto service-----"+dto);
        System.out.println("------Afficher id service----------"+id);
    // Vérifier que la ville existe
    if (!villeRepository.existsById(id)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
            String.format("No ville exists with this ID : %d", id));
    }

    // Vérifier que l'ID du DTO n'est pas null
    if (Objects.isNull(dto.getId())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
            "Already created ville cannot have null ID.");
    }
    
    // Vérifier que l'ID du path correspond à l'ID du DTO
    if (!dto.getId().equals(id)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
            String.format("Path ID (%d) does not match DTO ID (%d)", id, dto.getId()));
    }
  System.out.println("-------Avant mapping to entity-----");
    Ville ville = villeMapper.toEntity(dto);
    System.out.println("-------Après mapping, ville = " + ville);
    
    System.out.println("-------Avant save-----");
    Ville savedVille = villeRepository.save(ville);
    System.out.println("-------Après save, ville sauvegardée = " + savedVille);
    
    System.out.println("-------Avant mapping to DTO-----");
    VilleDto result = villeMapper.toDto(savedVille);
    System.out.println("-------Après mapping to DTO, result = " + result);
    
    return result;

}

    /**
     * Get Pays by id.
     *
     * @param id searched ticket id
     * @return found Ville object
     */
    public VilleDto findOne(final long id) {
        if (!villeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No ville exists with this ID : %d", id));
        }
        return villeMapper.toDto(villeRepository.findById(id).orElse(null));
    }

    /**
     * Fetch all Ville stored in DB.
     * @return list of {@link PaysDto}
     */
    public List<VilleDto> findAll() {
        return villeRepository.findAllByDeletedFalse().stream().map(villeMapper::toDto).toList();

    }

    /**
     * Remove a Pays by id if exists.
     *
     * @param id removed pays id.
     */
    public void delete(final long id) {
        villeRepository.findById(id).ifPresentOrElse(ville -> {
            ville.setDeleted(Boolean.TRUE);
            villeRepository.save(ville);
            traceService.writeAuditEvent( EntityAuditAction.DELETE, ObjetEntity.VILLE);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Cannot remove ville with ID : %d", id));
        });
    }

}

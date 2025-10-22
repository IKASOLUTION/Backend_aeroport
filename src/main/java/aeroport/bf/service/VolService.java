package aeroport.bf.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.domain.Ville;
import aeroport.bf.domain.Vol;
import aeroport.bf.domain.enums.StatutVol;
import aeroport.bf.dto.MenuActionDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.dto.VolDto;
import aeroport.bf.dto.mapper.VilleMapper;
import aeroport.bf.dto.mapper.VolMapper;
import aeroport.bf.repository.VilleRepository;
import aeroport.bf.repository.VolRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class VolService {
    private final VolRepository volRepository;
    private final VolMapper volMapper;
    private final TraceService traceService;
    private final VilleRepository villeRepository;
    private final VilleMapper villeMapper;

    /**
     * Save compagine.
     *
     * @param dto {@link VolDto}
     * @return saved Vol object
     */
    public VolDto create(final VolDto dto) {

        LocalDate date = LocalDate.now();
        String numero = generateProformaNumber(date);
        dto.setNumero(numero);
        dto.setDateSaisie(date);
        Vol vol = volMapper.toEntity(dto);
        if (dto.getVilleNomA() != null) {
            if (!isExisteByNom(dto.getVilleNomA())) {
                Ville villeA = new Ville();
                villeA.setNom(dto.getVilleNomA());
                villeA = villeRepository.save(villeA);
                vol.setVilleArrivee(villeA);
            } else {
                villeRepository.findByNomIgnoreCaseAndDeletedFalse(dto.getVilleNomA())
                        .ifPresent(ville -> vol.setVilleArrivee(ville));
                ;

            }

        }
        if (dto.getVilleNomD() != null) {
            if (!isExisteByNom(dto.getVilleNomA())) {
                Ville villeD = new Ville();
                villeD.setNom(dto.getVilleNomD());
                villeD = villeRepository.save(villeD);
                vol.setVilleDepart(villeD);
            } else {
                villeRepository.findByNomIgnoreCaseAndDeletedFalse(dto.getVilleNomA())
                        .ifPresent(ville -> vol.setVilleDepart(ville));
                ;

            }
        }
        return volMapper.toDto(volRepository.save(vol));
    }

    /**
     * Update existing Vol.
     *
     * @param dto {@link VolDto}
     * @param id
     * @return updated Vol object
     */
    public VolDto update(final VolDto dto, final long id) {

        if (!volRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No Vol exists with this ID : %d", id));
        }
        if (Objects.isNull(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already created Vol cannot have null ID.");
        }
        Vol vol = volMapper.toEntity(dto);
        if (dto.getVilleNomA() != null) {
            if (!isExisteByNom(dto.getVilleNomA())) {
                Ville villeA = new Ville();
                villeA.setNom(dto.getVilleNomA());
                villeA = villeRepository.save(villeA);
                vol.setVilleArrivee(villeA);
            } else {
                villeRepository.findByNomIgnoreCaseAndDeletedFalse(dto.getVilleNomA())
                        .ifPresent(ville -> vol.setVilleArrivee(ville));
                ;

            }

        }
        if (dto.getVilleNomD() != null) {
            if (!isExisteByNom(dto.getVilleNomA())) {
                Ville villeD = new Ville();
                villeD.setNom(dto.getVilleNomD());
                villeD = villeRepository.save(villeD);
                vol.setVilleDepart(villeD);
            } else {
                villeRepository.findByNomIgnoreCaseAndDeletedFalse(dto.getVilleNomA())
                        .ifPresent(ville -> vol.setVilleDepart(ville));

            }

        }
        return volMapper.toDto(volRepository.save(vol));
    }

    /**
     * Get Vol by id.
     *
     * @param id searched Hotel id
     * @return found VolDto object
     */
    public VolDto findOne(final long id) {
        if (!volRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No Vol exists with this ID : %d", id));
        }
        return volMapper.toDto(Objects.requireNonNull(volRepository.findById(id).orElse(null)));
    }

    /**
     * Fetch page Vol stored in DB.
     * 
     * @param dto of {@link VolDto}
     * @return page of {@link VolDto}
     */

    /**
     * Fetch all Vol stored in DB.
     * 
     * @return list of {@link VolDto}
     */
    public List<VolDto> findAll() {
        return volMapper.toDto(volRepository.findAllByDeletedFalse());

    }

    public Page<VolDto> findAllPeriodeAndStatut(LocalDate startDate, LocalDate endDate, Pageable pageable,
            List<StatutVol> statutVols) {
        Page<Vol> vols = volRepository.findByDeletedFalseAndStatutInAndDateDepartBetween(
                statutVols,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59),
                pageable);
        return vols.map(vol -> volMapper.toDto(vol));
    }

    /**
     * Remove a Vol by id if exists.
     *
     * @param id removed hotel id.
     */
    public void delete(final long id) {
        volRepository.findById(id).ifPresentOrElse(vol -> {
            vol.setDeleted(Boolean.TRUE);
            volRepository.save(vol);
            traceService.writeAuditEvent(EntityAuditAction.VOL, ObjetEntity.VOL);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Cannot remove Vol with ID : %d", id));
        });
    }

    public String generateProformaNumber(LocalDate now) {
        if (now == null) {
            now = LocalDate.now();
        }
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        // Compter les proformas du jour courant basé sur dateSaisie
        int sequenceNumber = volRepository.countByDateSaisie(now) + 1;

        // Générer la lettre de séquence
        String sequenceLetter = generateSequenceLetter(sequenceNumber);

        // Format final: PROF-YYYY-MM-DD-X
        String numero = sequenceLetter + String.format("%02d", day) + String.format("%02d", month)
                + String.format("%02d", year % 100);

        return numero;
    }

    private static String generateSequenceLetter(int sequenceNumber) {
        StringBuilder result = new StringBuilder();

        while (sequenceNumber > 0) {
            sequenceNumber--;
            result.insert(0, (char) ('A' + (sequenceNumber % 26)));
            sequenceNumber /= 26;
        }

        return result.toString();
    }

    public Boolean isExisteByNom(final String nom) {
        Boolean isExiste = villeRepository.existsByDeletedFalseAndNomIgnoreCase(nom);

        return isExiste;
    }

    public List<VilleDto> findVilleByPays(Long paysId) {
        return villeMapper.toDto(villeRepository.findAllByPaysIdAndDeletedFalse(paysId));
    }

    public List<VilleDto> findVille() {
        return villeMapper.toDto(villeRepository.findAllByDeletedFalse());
    }
}

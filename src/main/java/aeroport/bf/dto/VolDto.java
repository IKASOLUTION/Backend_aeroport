package aeroport.bf.dto;

import aeroport.bf.domain.enums.StatutVol;
import aeroport.bf.domain.enums.TypeVol;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)

public class VolDto extends AbstractAuditEntityDto  implements Serializable {
   
    private Long id;
    
    private String numero;
   
    private VilleDto villeDepart;
   
    private VilleDto villeArrivee;
   
    private TypeVol typeVol;
   
    private CompagnieDto compagnie;

    private Long compagnieId;
    private Long aeroportId;
    private Long villeDepartId;
    private Long villeArriveeId;

    private AeroportDto aeroport;

    private LocalDateTime dateDepart;

    private LocalDateTime dateArrivee;

    private StatutVol statut;

    private LocalDate dateSaisie;

    private String villeNomD;
    
    private String villeNomA;
   

   
} 

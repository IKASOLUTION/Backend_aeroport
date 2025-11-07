package aeroport.bf.dto;

import aeroport.bf.domain.Pays;
import aeroport.bf.domain.Ville;
import aeroport.bf.domain.enums.Statut;
import aeroport.bf.domain.enums.StatutAeroport;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class AeroportDto extends AbstractAuditEntityDto implements Serializable{
    private Long id;

    private String nomAeroport;

    private StatutAeroport statutAeroport;

    private Ville ville;

    private Pays pays;

    
}

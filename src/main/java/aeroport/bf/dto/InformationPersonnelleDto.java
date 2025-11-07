package aeroport.bf.dto;

import aeroport.bf.domain.AbstractAuditEntity;
import aeroport.bf.domain.enums.Statut;
import aeroport.bf.domain.enums.TypeDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * A compagnie.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class InformationPersonnelleDto extends AbstractAuditEntityDto implements Serializable {
    
   private Long id;
    private String nomFamille;
    private String prenom;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String nationalite;
    private TypeDocument  typeDocument;
    private String numeroDocument;
    private LocalDate dateDelivrance;
    private String lieuDelivrance;
    private String numeroNip;
    private String profession;
    private String adresseBurkina;
    private String telephoneBurkina;
    private String paysResidence;
    private String emailContact;
    @Builder.Default
    private Statut statut = Statut.ACTIF;
    
}

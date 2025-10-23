package aeroport.bf.dto;

import aeroport.bf.domain.AbstractAuditEntity;
import aeroport.bf.domain.enums.Statut;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * A liste_noire.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ListeNoireDto extends AbstractAuditEntity implements Serializable {

    private Long id;

    /**
     * Nom du client
     */
    private String nom;

    /**
     * Prenom du client
     */
    private String prenom;

    /**
     * Date de naissance du client
     */
    private LocalDate dateNaissance;

    /**
     * Lieu de naissance du client
     */
    private String lieuNaissance;

    /**
     * Motif d'interdiction du client fourni
     */
    private String motif;

    /**
     * Chemin du photo du client fourni
     */
    private String photo;

    /**
     * Statut
     */
     private Statut statut;

    private DonneeBiometriqueDto donneeBiometrique;

}

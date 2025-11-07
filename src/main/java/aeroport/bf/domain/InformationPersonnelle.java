package aeroport.bf.domain;

import aeroport.bf.domain.enums.Statut;
import aeroport.bf.domain.enums.StatutVol;
import aeroport.bf.domain.enums.StatutVoyageur;
import aeroport.bf.domain.enums.TypeVol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "informations_personnelles")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class InformationPersonnelle extends AbstractAuditEntity  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "informations_personnelle_seq_generator")
    @SequenceGenerator(name = "informations_personnelle_seq_generator", sequenceName = "informations_personnelle_sequence",
            initialValue = 1001, allocationSize = 1)
    private Long id;

   
    @Column(name = "nom_famille", length = 100, nullable = false)
    private String nomFamille;

    @Column(name = "prenom", length = 100, nullable = false)
    private String prenom;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance", length = 100, nullable = false)
    private String lieuNaissance;

    @Column(name = "nationalite", length = 50, nullable = false)
    private String nationalite;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_document", nullable = false, length = 30)
    private TypeDocument typeDocument;

    @Column(name = "numero_document", length = 50, nullable = false)
    private String numeroDocument;

    @Column(name = "date_delivrance", nullable = false)
    private LocalDate dateDelivrance;

    @Column(name = "lieu_delivrance", length = 100, nullable = false)
    private String lieuDelivrance;

    @Column(name = "numero_nip", length = 50)
    private String numeroNip;

    @Column(name = "profession", length = 100, nullable = false)
    private String profession;

    @Column(name = "adresse_burkina", columnDefinition = "TEXT")
    private String adresseBurkina;

    @Column(name = "telephone_burkina", length = 20)
    private String telephoneBurkina;

    @Column(name = "pays_residence", length = 50, nullable = false)
    private String paysResidence;

    @Column(name = "email_contact", length = 255)
    private String emailContact;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 20)
    private Statut statut = Statut.ACTIF;

   
    
}

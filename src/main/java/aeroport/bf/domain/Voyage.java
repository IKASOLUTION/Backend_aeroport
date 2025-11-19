package aeroport.bf.domain;

import aeroport.bf.domain.enums.EtatVoyage;
import aeroport.bf.domain.enums.MotifVoyage;
import aeroport.bf.domain.enums.Statut;
import aeroport.bf.domain.enums.StatutVol;
import aeroport.bf.domain.enums.StatutVoyage;
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
import java.time.LocalTime;


/**
 * A compagnie.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "voyages")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Voyage extends AbstractAuditEntity  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voyage_seq_generator")
    @SequenceGenerator(name = "voyage_seq_generator", sequenceName = "voyage_sequence",
            initialValue = 1001, allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vol_id", nullable = false)
    private Vol vol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ville_depart_id", nullable = false)
    private Ville villeDepart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ville_destination_id", nullable = false)
    private Ville villeDestination;

    @Enumerated(EnumType.STRING)
    @Column(name = "motif_voyage", nullable = false, length = 50)
    private MotifVoyage motifVoyage;

    @Column(name = "date_voyage", nullable = false)
    private LocalDate dateVoyage;

    @Column(name = "heure_voyage", nullable = false)
    private LocalTime heureVoyage;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_voyage", nullable = false, length = 50)
    private EtatVoyage etatVoyage;

    @Column(name = "duree_sejour", nullable = false)
    private Integer dureeSejour;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 50)
    @Builder.Default
    private StatutVoyage statut = StatutVoyage.ACTIF;

    @ManyToOne
    @JoinColumn(name = "aeroport_id", referencedColumnName = "id")
    private Aeroport aeroport;
    
}

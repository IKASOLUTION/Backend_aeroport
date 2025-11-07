package aeroport.bf.domain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;


/**
 * A donnee_biometrique.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "donnee_biometrique")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class DonneeBiometrique extends AbstractAuditEntity  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donnee_biometrique_seq_generator")
    @SequenceGenerator(name = "donnee_biometrique_seq_generator", sequenceName = "donnee_biometrique_sequence",
            initialValue = 1001, allocationSize = 1)
    private Long id;
    @Column(name = "empreinte_gauche")
    private Boolean empreinteGauche;
    @Column(name = "empreinte_droite")
    private Boolean empreinteDroite;
    @Column(name = "empreinte_pouces")
    private Boolean empreintePouces;
    @Column(name = "photo_biometrique_path")
    private String photoBiometriquePath;
    @Column(name = "exercice")
    private Long exercice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enregistrement_id", nullable = false)
    private Enregistrement enregistrement;

}

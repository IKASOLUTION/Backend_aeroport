package aeroport.bf.domain;

import aeroport.bf.domain.enums.Statut;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
 * A compagnie.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aeroport")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Aeroport extends AbstractAuditEntity  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aeroport_seq_generator")
    @SequenceGenerator(name = "aeroport_seq_generator", sequenceName = "aeroport_sequence",
            initialValue = 1001, allocationSize = 1)
    private Long id;

    @Column(name = "nom_aeroport", nullable = false, unique = true)
    @NotNull
    private String nomAeroport;

    @Column(name = "statut")
    @Enumerated(EnumType.STRING)
    private Statut statut;


    private String ville;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("aeroport")
    private Pays pays;
}

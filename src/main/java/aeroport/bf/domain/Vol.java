package aeroport.bf.domain;

import aeroport.bf.domain.enums.Statut;
import aeroport.bf.domain.enums.StatutVol;
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
@Table(name = "vols")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Vol extends AbstractAuditEntity  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vol_seq_generator")
    @SequenceGenerator(name = "vol_seq_generator", sequenceName = "vol_sequence",
            initialValue = 1001, allocationSize = 1)
    private Long id;

   

    @Column(name = "numero_vol")
    @NotNull
    private String numero;
    @Column(name = "date_depart")
    @NotNull
    private LocalDateTime dateDepart;
    @Column(name = "date_arrivee")
    @NotNull
    private LocalDateTime dateArrivee;
    @Column(name = "date_saisie")
    private LocalDate dateSaisie;
    @Column(name = "type_vol")
    @Enumerated(EnumType.STRING)
    @NotNull
    private TypeVol typeVol;
    @Column(name = "statut")
    @Enumerated(EnumType.STRING)
    @NotNull
    @Builder.Default
    private StatutVol statut = StatutVol.PROGRAMME;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("vol")
    private Compagnie compagnie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("vol")
    private Aeroport aeroport; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("vol")
    private Aeroport aeroportForUser;
    
}

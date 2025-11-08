package aeroport.bf.domain;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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



/**
 * Entité PieceJointe.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pieces_jointes")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class PieceJointe extends AbstractAuditEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "piece_jointe_seq_generator")
    @SequenceGenerator(name = "piece_jointe_seq_generator", sequenceName = "piece_jointe_sequence",
            initialValue = 1001, allocationSize = 1)
    private Long id;
    
    @NotNull
    @Column(name = "chemin_img_recto", nullable = false)
    private String cheminImgRecto;
    @NotNull
    @Column(name = "chemin_img_verso", nullable = false)
    private String cheminImgVerso;
    @NotNull
    @Column(name = "chemin_photo_profil", nullable = false)
    private String cheminPhotoProfil;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "information_personnel_id", nullable = false)
    private InformationPersonnelle informationPersonnel;

   
}

package aeroport.bf.domain;

import aeroport.bf.domain.enums.Statut;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "compagnie")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Compagnie extends AbstractAuditEntity  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compagnie_seq_generator")
    @SequenceGenerator(name = "compagnie_seq_generator", sequenceName = "compagnie_sequence",
            initialValue = 1001, allocationSize = 1)
    private Long id;

    @Column(name = "nom_compagnie", nullable = false)
    @NotNull
    private String nomCompagine;

    @Column(name = "statut")
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Column(name = "groupe")
    private String groupe;

    @Column(name = "siege")
    private String siege;

    @Column(name = "contact")
    private String contact;

    @Email
    @Size(min = 4, max = 254)
    @Column(name="email", length = 254, unique = true)
    private String email;

    @Column(name = "adresse_siege")
    private String adresseSiege;

     @Column(name = "pays")
    private String pays;

    @Column(name = "nom_responsable")
    private String nomResponsable;
    
     @Column(name = "prenom_responsable")
    private String prenomResponsable;
    
     @Column(name = "nationalite_responsable")
    private String nationaliteResponsable;
    
     @Column(name = "telephone_responsable")
    private String telephoneResponsable;
    
     @Column(name = "mail_responsable")
    private String mailResponsable;

    @ManyToOne
    @JoinColumn(name = "aeroport_id", referencedColumnName = "id")
    private Aeroport aeroport;


}

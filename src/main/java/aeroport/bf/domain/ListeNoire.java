package aeroport.bf.domain;

import aeroport.bf.domain.enums.Statut;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


/**
 * A liste_noire.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "liste_noire")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ListeNoire extends AbstractAuditEntity  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "liste_noire_seq_generator")
    @SequenceGenerator(name = "liste_noire_seq_generator", sequenceName = "liste_noire_sequence",
            initialValue = 1001, allocationSize = 1)
    private Long id;

    /**
     * Nom du client
     */
    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    /**
     * Prenom du client
     */
    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    /**
     * Date de naissance du client
     */
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

   
    /**
     * Lieu de naissance du client
     */
    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    /**
     * Motif d'interdiction du client fourni
     */
    @NotNull
    @Column(name = "motif", nullable = false, columnDefinition = "TEXT")
    private String motif;

    /**
     * Chemin du photo du client fourni
     */
    @Column(name = "photo")
    private String photo;

    /**
     * Statut
     */
    @Column(name = "statut")
     @Enumerated(EnumType.STRING)
     private Statut statut;

    @Column(name = "numero_nip", length = 50)
    private String numeroNip;

    @Column(name = "numero_cnib")
    private String numeroCnib;


}

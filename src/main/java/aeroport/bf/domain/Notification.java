package aeroport.bf.domain;

import aeroport.bf.domain.enums.Statut;
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
 * A Pays.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifaction")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification extends AbstractAuditEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifaction_se_generator")
    @SequenceGenerator(name = "notifaction_se_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle",length=1024)
    private String libelle;

    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @Column(name = "prenom", length = 100, nullable = false)
    private String prenom;
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

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    // nouveau champ date
    @Column(name = "date_notification")
    private LocalDate dateNotification;

    @Column(name = "lieu_naissance", length = 100, nullable = false)
    private String lieuNaissance;

    @ManyToOne
    @JoinColumn(name = "aeroport_id", referencedColumnName = "id")
    private Aeroport aeroport;
}

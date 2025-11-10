package aeroport.bf.dto;

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

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class NotificationDto extends AbstractAuditEntityDto implements Serializable{
    private Long id;

    private String libelle;

    private String nom;

    private String prenom;
    /**
     * Statut
     */
    private Statut statut;

    private String numeroNip;

    private String numeroCnib;

    private LocalDate dateNaissance;

    private String lieuNaissance;
}

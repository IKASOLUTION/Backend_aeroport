package aeroport.bf.dto;

import aeroport.bf.domain.AbstractAuditEntity;
import aeroport.bf.domain.Voyage;
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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true,exclude = {"utilisateur","voyage"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class EnregistrementDto extends AbstractAuditEntityDto implements Serializable {
    
    private Long id;

   
   
    private UserDto utilisateur;

   
    private VoyageDto voyage; 

    private String adresseEtranger;

    private String telephoneEtranger;


    @Builder.Default
    private StatutVoyageur statut = StatutVoyageur.EN_ATTENTE;
    
}

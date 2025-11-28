package aeroport.bf.dto;

import aeroport.bf.domain.Ville;
import aeroport.bf.domain.enums.StatutAeroport;
import aeroport.bf.domain.enums.TypeAeroport;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class AeroportDto extends AbstractAuditEntityDto implements Serializable {

    private Long id;

    private String nomAeroport;

    private StatutAeroport statutAeroport;

    private Long villeId;

    private Ville ville;

    private String pays;
    private String siteWeb;
    private TypeAeroport typeAeroport;
    private String adresse;
    private String Code;
    private Double latitude;
    private Double longitude;
    private String telephone;
    private String code_oaci;
    private String nomResponsable;
    private String prenomResponsable;
    private String mailResponsable;
    private String telephoneResponsable;
}
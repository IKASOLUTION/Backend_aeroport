package aeroport.bf.dto;

import aeroport.bf.domain.Ville;
import aeroport.bf.domain.enums.StatutAeroport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class AeroportDto extends AbstractAuditEntityDto implements Serializable {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("nomAeroport")
    private String nomAeroport;
    
    @JsonProperty("statutAeroport")
    private StatutAeroport statutAeroport;
    
    @JsonProperty("villeId")
    private Long villeId;
    
    @JsonIgnore
    private Ville ville;
    
    @JsonProperty("pays")
    private String pays;
   private String typeAeroport;
   private String siteWeb;
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
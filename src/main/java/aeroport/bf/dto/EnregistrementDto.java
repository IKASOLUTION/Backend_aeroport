package aeroport.bf.dto;

import aeroport.bf.domain.AbstractAuditEntity;
import aeroport.bf.domain.Ville;
import aeroport.bf.domain.Voyage;
import aeroport.bf.domain.enums.EtatVoyage;
import aeroport.bf.domain.enums.MotifVoyage;
import aeroport.bf.domain.enums.Statut;
import aeroport.bf.domain.enums.StatutVol;
import aeroport.bf.domain.enums.StatutVoyageur;
import aeroport.bf.domain.enums.TypeDocument;
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
import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;


/**
 * A compagnie.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true,exclude = {"voyage","informationPersonnelle", "photoProfil", "imageRecto", "imageVerso"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class EnregistrementDto extends AbstractAuditEntityDto implements Serializable {
    
    private Long id;
   
    private VoyageDto voyage; 

    private InformationPersonnelleDto informationPersonnelle;


    @Builder.Default
    private StatutVoyageur statut = StatutVoyageur.EN_ATTENTE;
     // === Document ===
    private TypeDocument typeDocument; // 'PASSEPORT' | 'CNI' | 'PERMIS_CONDUIRE'
    private String numeroDocument;
    private String numeroNip;
    private LocalDate dateDelivrance;
    private String lieuDelivrance;
    private Long exercice;;
    private MultipartFile  photoProfil;
    private MultipartFile  imageRecto;
    private MultipartFile  imageVerso;
    private Long informationPersonnelleId;

    // === Personal Info ===
    private String nomFamille;
    private String prenom;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String nationalite;
    private String profession;

    // === Coordonnees ===
    private String paysResidence;
    private String emailContact;
    private String telephoneBurkina;
    private String telephoneEtranger;
    private String adresseBurkina;
    private String adresseEtranger;
    private LocalDate dateSaisie;
    private Long aeroportId;
    private Long volId;

    private Long nbreVoyage;

     private String villeDepart;

    private String villeDestination;

    
    private MotifVoyage motifVoyage;

   
    private LocalTime heureVoyage;

    private EtatVoyage etatVoyage;

    private Integer dureeSejour; 
    private LocalDate dateVoyage;
     private String aeroportDepart;
    private String aeroportDestination;

    
}
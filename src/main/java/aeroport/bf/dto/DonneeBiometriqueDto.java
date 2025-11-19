package aeroport.bf.dto;
import aeroport.bf.domain.AbstractAuditEntity;
import aeroport.bf.domain.enums.StatutDonneeBio;
import aeroport.bf.domain.enums.TypeCapture;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;


/**
 * A donnee_biometrique.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"photoBiometrique"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class DonneeBiometriqueDto extends AbstractAuditEntity implements Serializable {
    private Long id;

    private Boolean empreinteGauche;

    private Boolean empreinteDroite;

    private Boolean empreintePouces;

    private Long enregistrementId;

    private Long aeroportId;

    private Long exercice;;

    private MultipartFile photoBiometrique;

     private String photoBiometriquePath;

    private TypeCapture typeCapture;

    private StatutDonneeBio statut;

    private EnregistrementDto enregistrement;

    private InformationPersonnelleDto informationPersonnelle;

    private AeroportDto aeroport;
}

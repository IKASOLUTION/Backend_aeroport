package aeroport.bf.dto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public  class DocumentData {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String documentNumber;
    private String expiryDate;
    private String nationality;
    // private String gender;
    private String placeOfBirth;
    private String issuingCountry;
    private String issueDate;
    private String mrzText;
    private String documentType;
    private String lieuNaissance;
    private String sexe;
    private String profession;
    private String issueState;
    private String nip;
    private String dateIssue;
    
    }


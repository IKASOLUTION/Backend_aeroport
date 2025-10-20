package aeroport.bf.dto;
import java.util.Set;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
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
public class ExerciceDto  extends AbstractAuditEntityDto {

    private Long id;
    private Long annee;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Boolean active;
  

    
}

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
public class MenuActionDto  extends AbstractAuditEntityDto {

    private Long id;

    private String menuActionLibelle;

    private String menuActionCode;

    private Boolean deleted;

    private Long moduleParamId;

    private String moduleParamLibelle;
    
}

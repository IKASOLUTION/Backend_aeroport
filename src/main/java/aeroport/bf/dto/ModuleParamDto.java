package aeroport.bf.dto;
import java.util.List;

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
public class ModuleParamDto  extends AbstractAuditEntityDto {
    
    private Long id;
    
    private String moduleParamLibelle;

    private String moduleParamCode;

    private List<MenuActionDto> menuActions;
}

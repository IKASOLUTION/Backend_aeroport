package aeroport.bf.dto;

import java.util.Set;
import java.io.Serializable;
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
import aeroport.bf.domain.MenuAction;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ProfilDto  extends AbstractAuditEntityDto implements Serializable{

    private Long id;

    private String code;

    private String libelle;
   
    //private Boolean deleted;
    private Set<MenuAction> menus;

    
}

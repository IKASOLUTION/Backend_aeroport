package aeroport.bf.domain;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "module")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ModuleParam extends AbstractAuditEntity{

        @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "ModuleParam_sequence",
            initialValue = 1001, allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "moduleParamLibelle", nullable = false)

    private String moduleParamLibelle;
    @NotNull
    @Column(name = "moduleParamCode", nullable = false)
    private String moduleParamCode;

   
      @OneToMany(mappedBy = "moduleParam", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"moduleParam", "hibernateLazyInitializer"}, allowSetters = true)
    @Builder.Default
    private Set<MenuAction> menuActions = new HashSet<>();

    // Méthodes utilitaires pour gérer la relation bidirectionnelle
    public void addMenuAction(MenuAction menuAction) {
        if (menuActions == null) {
            menuActions = new HashSet<>();
        }
        menuActions.add(menuAction);
        menuAction.setModuleParam(this);
    }

    public void removeMenuAction(MenuAction menuAction) {
        if (menuActions != null) {
            menuActions.remove(menuAction);
            menuAction.setModuleParam(null);
        }
    }
    
}

package aeroport.bf.domain;
import java.io.Serial;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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

   
     @OneToMany(mappedBy = "moduleParam")
     private Set<MenuAction> menuActions;
    
}

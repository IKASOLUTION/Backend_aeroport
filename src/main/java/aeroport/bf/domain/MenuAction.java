package aeroport.bf.domain;
import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "menu_action")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class MenuAction extends AbstractAuditEntity {

        @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menuAction_seq_generator")
    @SequenceGenerator(name = "menuAction_seq_generator", sequenceName = "ennov_menuAction_sequence",
            initialValue = 1001, allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "menuActionLibelle", nullable = false)
    private String menuActionLibelle;

    @NotNull
    @Column(name = "menuActionCode", nullable = false)
    private String menuActionCode;

   /*  @Column(name = "deleted")
    private Boolean deleted; */

    @NotNull
     @ManyToOne
    @JoinColumn(name = "moduleParam_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "menuActions", allowSetters = true)
    private ModuleParam moduleParam; 

    
}

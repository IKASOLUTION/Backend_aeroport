package aeroport.bf.dto;

import aeroport.bf.domain.AbstractAuditEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResponsableDto extends AbstractAuditEntity implements Serializable {

    private Long id;

    private String nom;

    private String prenom;

    private String nationalite;

    private String telephone;

    private String email;
}

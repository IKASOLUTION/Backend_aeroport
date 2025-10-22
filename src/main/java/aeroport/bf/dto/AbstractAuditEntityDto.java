package aeroport.bf.dto;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.springframework.data.annotation.ReadOnlyProperty;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractAuditEntityDto implements Serializable {

    @ReadOnlyProperty
    private String createUser;

    @ReadOnlyProperty
    private Instant dateCreation;

    @ReadOnlyProperty
    private String lastModifyUser;

    @ReadOnlyProperty
    private Instant dateLastModification;

    @ReadOnlyProperty
    private Boolean isDeleted = false;  
}

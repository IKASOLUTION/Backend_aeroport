package aeroport.bf.domain;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import aeroport.bf.config.audit.EntityAuditEventListener;
import aeroport.bf.config.security.SecurityUtils;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Audited
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractAuditEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "deleted")
    private Boolean deleted; 

}

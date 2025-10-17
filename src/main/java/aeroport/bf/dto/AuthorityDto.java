package aeroport.bf.dto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;

@Data
@Builder
public class AuthorityDto {

    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    @EqualsAndHashCode.Include
    private String name;

    private String description;
}

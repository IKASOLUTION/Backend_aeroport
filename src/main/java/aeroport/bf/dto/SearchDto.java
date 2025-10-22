package aeroport.bf.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import aeroport.bf.domain.enums.StatutVol;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
public class SearchDto {

    private Long id;
  
    private LocalDate dateDebut;
    private LocalDate dateFin;
     private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
    private List<StatutVol> statutVols;

    
}

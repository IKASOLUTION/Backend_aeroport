package aeroport.bf.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
public class TraceDto  {
    private Long id;
    private String entity;
    private String action;
    private LocalDateTime dateSaisie;
    private LocalDate dateD;
    private LocalDate dateF;
    private UserDto user;
    private  String objet;
   // private Boolean deleted;
}

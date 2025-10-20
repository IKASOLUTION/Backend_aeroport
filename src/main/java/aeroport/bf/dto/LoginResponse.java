package aeroport.bf.dto;

import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public  class  LoginResponse {
    
    private HttpHeaders headers;
    private JWTToken body;
    

    
}


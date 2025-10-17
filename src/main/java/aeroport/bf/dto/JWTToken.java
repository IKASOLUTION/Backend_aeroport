package aeroport.bf.dto;
import java.util.Map;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public  class  JWTToken {
    
    private String accessToken;
    private String  refreshToken;
    public JWTToken(Map<String,String> token) {
        this.accessToken = token.get("access_token");
        this.refreshToken = token.get("refresh_token");
    }
/*     private String  expires_in;
 */    
}


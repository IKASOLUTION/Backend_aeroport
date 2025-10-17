package aeroport.bf.config.security.jwt;

import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Data
@Component
public class TokenProvider {

    @Value("${spring.security.authentication.jwt.base64-secret}")
    private String secretKey;

    @Value("${spring.security.authentication.jwt.token-validity-in-seconds}")
    private long tokenValidity;

    @Value("${spring.security.authentication.jwt.token-validity-in-seconds-for-remember-me}")
    private long tokenValidityForRememberMe;
    
    @Value("${spring.security.authentication.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidity;

    private static final String AUTHORITIES_KEY = "auth";

    private SecretKey key;

    @PostConstruct
    public void init() {
        // Convertir les secondes en millisecondes
        this.tokenValidity *= 1000;
        this.tokenValidityForRememberMe *= 1000;
        this.refreshTokenValidity *= 1000;

        try {
            // Essayer de décoder comme Base64
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            
            // Vérifier que la clé a au moins 256 bits (32 bytes)
            if (keyBytes.length < 32) {
                log.warn("Secret key too short ({}), padding to 256 bits", keyBytes.length * 8);
                // Si trop courte, utiliser la chaîne directement et la hasher
                keyBytes = secretKey.getBytes();
            }
            
            // Si la clé est trop longue (> 512 bits), la tronquer
            if (keyBytes.length > 64) {
                byte[] truncated = new byte[64];
                System.arraycopy(keyBytes, 0, truncated, 0, 64);
                keyBytes = truncated;
            }
            
            // Si la clé est trop courte (< 256 bits), la remplir
            if (keyBytes.length < 32) {
                byte[] padded = new byte[32];
                System.arraycopy(keyBytes, 0, padded, 0, keyBytes.length);
                keyBytes = padded;
            }
            
            key = Keys.hmacShaKeyFor(keyBytes);
            log.info("JWT Token Provider initialized successfully with validity: {} ms", tokenValidity);
            
        } catch (Exception e) {
            log.error("Failed to initialize JWT key from base64-secret: {}", e.getMessage());
            // Fallback: utiliser directement les bytes de la chaîne
            byte[] keyBytes = secretKey.getBytes();
            if (keyBytes.length < 32) {
                // Remplir à 32 bytes minimum
                byte[] padded = new byte[32];
                System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
                keyBytes = padded;
            }
            key = Keys.hmacShaKeyFor(keyBytes);
            log.warn("JWT Token Provider initialized with fallback key");
        }
    }

    /**
     * Génère un access token JWT
     *
     * @param userDetails les détails de l'utilisateur
     * @param rememberMe si l'utilisateur veut rester connecté
     * @return le token JWT
     */
    public String generateJwtToken(final UserDetails userDetails, boolean rememberMe) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        long validityPeriod = rememberMe ? this.tokenValidityForRememberMe : this.tokenValidity;
        Date validity = new Date(now + validityPeriod);
        
        System.out.println("=======validityPeriod=========="+validityPeriod);
        System.out.println("=======validity=========="+validity); 
        System.out.println("=======now=========="+now);

        String token = Jwts.builder()
                .claim(AUTHORITIES_KEY, authorities)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(key)
                .compact();
                
        log.debug("Generated JWT token for user: {} with expiration: {}", 
                  userDetails.getUsername(), validity);
        
        return token;
    }

    /**
     * Génère un refresh token
     *
     * @param userDetails les détails de l'utilisateur
     * @return le refresh token
     */
    public String generateRefreshToken(final UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        long now = System.currentTimeMillis();
        Date validity = new Date(now + this.refreshTokenValidity);
    
        String refreshToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .claim("type", "refresh")
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key)
                .compact();
                
        log.debug("Generated refresh token for user: {} with expiration: {}", 
                  userDetails.getUsername(), validity);
        
        return refreshToken;
    }

    /**
     * Valide un token avec les détails utilisateur
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("Token validation failed for user {}: {}", 
                      userDetails.getUsername(), e.getMessage());
            return false;
        }
    }

    /**
     * Valide un token sans détails utilisateur
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("Malformed token: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported token: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.error("Invalid token signature: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrait le username du token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait la date d'expiration du token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrait un claim spécifique du token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrait tous les claims du token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Vérifie si le token est expiré
     */
    private boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            boolean expired = expiration.before(new Date());
            
            if (expired) {
                log.warn("Token expired at: {}", expiration);
            }
            
            return expired;
        } catch (ExpiredJwtException e) {
            log.warn("Token already expired: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Génère à la fois un access token et un refresh token
     *
     * @param userDetails les détails de l'utilisateur
     * @param rememberMe si l'utilisateur veut être rappelé
     * @return Map contenant access_token et refresh_token
     */
    public Map<String, String> generateTokenPair(final UserDetails userDetails, boolean rememberMe) {
        Map<String, String> tokens = new HashMap<>();
        
        // Générer l'access token
        String accessToken = generateJwtToken(userDetails, rememberMe);
        
        // Générer le refresh token
        String refreshToken = generateRefreshToken(userDetails);
        
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        
        log.info("Generated token pair for user: {}", userDetails.getUsername());
        
        return tokens;
    }
    
    /**
     * Extrait les autorités du token
     */
    public String getAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get(AUTHORITIES_KEY, String.class);
    }
}
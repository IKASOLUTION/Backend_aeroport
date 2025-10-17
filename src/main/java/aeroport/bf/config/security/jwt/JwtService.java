package aeroport.bf.config.security.jwt;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
   
    private static final String AUTHORITIES_KEY = "auth";


    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;
    private static final String SECRET_KEY ="ZDAwYzc4Nzc3NjBhMDllMDYyZWNkZGI3MGExOWM2OWIxYzY5NjhmNTJhOGQ2MDIzNWI3OWE0YTg1YzY5NzVhNDQ4ZWI0NDJmNTk0OTE2NmRkMjZjN2EyY2ZiODVjYjU5OGUyMzA3YTVkOWFlOGQ3YmY1YzFlNDYyNmEyZjYzM2Y=";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim( String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
       
    }


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }



    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(getSignInbKey(), SignatureAlgorithm.HS512)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 720))
            .compact();

       
    }


    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails) {
        return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 720))
        .signWith(getSignInbKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && isTokenExpired(token));

    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
        .parserBuilder()
        .setSigningKey(getSignInbKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }
    private Key getSignInbKey() {
       byte[] kyeBytes = Decoders.BASE64.decode(SECRET_KEY);
       return Keys.hmacShaKeyFor(kyeBytes);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInbKey()).build().parseClaimsJws(authToken);
           // System.out.println("Invalid JWT signature. 12222");
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT signature.");
            System.out.println("Invalid JWT signature trace: {} 1"+ e);
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token.");
            System.out.println("Expired JWT token trace: {}"+ e);
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token.");
            System.out.println("Unsupported JWT token trace: {}"+ e);
        } catch (IllegalArgumentException e) {
            System.out.println("JWT token compact of handler are invalid.");
            System.out.println("JWT token compact of handler are invalid trace: {}"+ e);
        }
        return false;
    }


    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(getSignInbKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    
}

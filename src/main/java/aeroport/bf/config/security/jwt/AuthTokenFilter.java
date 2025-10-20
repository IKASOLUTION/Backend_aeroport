package aeroport.bf.config.security.jwt;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import aeroport.bf.config.security.TestUserDetailsService;
import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    TestUserDetailsService userDetailsService;

    /**
     * Filter incoming request based on user auth.
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws jakarta.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = resolveToken(request);

            if (StringUtils.hasText(jwt)) {
                System.out.println("=======jwt=========="+jwt);
                String username = tokenProvider.extractUsername(jwt);
                System.out.println("=======username=========="+username);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("=======userDetails=========="+userDetails);

                if (tokenProvider.validateToken(jwt, userDetails)) {
                    System.out.println("=======tokenProvider===1=====Laurent=="+tokenProvider);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }


            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.info("Security exception for user {} - {}",
                    e.getClaims().getSubject(), e.getMessage());

            log.trace(String.valueOf(e));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        System.out.println("=======token=========="+bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return "";
    }
}

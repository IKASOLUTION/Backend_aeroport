package aeroport.bf.config.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    /**
     * Check access resources using user authentication.
     *
     * @param request
     * @param response
     * @param e
     */
    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException e) throws IOException {
        log.error("Unauthorized error: {}", e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getLocalizedMessage());
    }
}

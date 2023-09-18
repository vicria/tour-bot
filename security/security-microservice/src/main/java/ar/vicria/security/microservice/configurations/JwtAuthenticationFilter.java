package ar.vicria.security.microservice.configurations;

import ar.vicria.security.microservice.services.SecurityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private SecurityUserService securityUserService;

//    @Value("${security.token.type}")
    private String tokenType = "Bearer";

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String AUTHORIZATION_REQUEST_PARAM = "auth_token";

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Integer userId = tokenProvider.getUserIdFromJwt(jwt);

                UserDetails userDetails = securityUserService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String headerBearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(headerBearerToken) && headerBearerToken.startsWith(tokenType)) {
            return headerBearerToken.substring(tokenType.length()).trim();
        }
        return request.getParameter(AUTHORIZATION_REQUEST_PARAM);
    }
}

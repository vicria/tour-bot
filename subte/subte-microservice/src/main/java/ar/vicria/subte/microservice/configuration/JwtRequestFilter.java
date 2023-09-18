package ar.vicria.subte.microservice.configuration;

import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    //private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username = null;
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                //username = jwtTokenUtils.getUserName(jwt); //todo поменять на Optional
                username = "name";
            //} catch (SignatureException e) {
            //log.info("Signature is incorrect");
            } catch (Exception e) {
                log.info("Token is expired");
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        //jwtTokenUtils.getAuthorities(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                        Stream.of("ADMIN").map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
                SecurityContextHolder.getContext().setAuthentication(token);
            }
            filterChain.doFilter(request, response);
        }
    }
}


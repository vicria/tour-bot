package ar.vicria.security.microservice.configurations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

//    @Value("${security.token.secret}")
    private String jwtSecret = "secret";

//    @Value("${security.token.expirationInMs}")
    private int jwtExpirationInMs = 5000000;

    /**
     * Генерирует токен.
     *
     * @param authentication параметры аутификации
     * @return Токен аутификации
     */
    public String generateToken(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
            .setSubject(Long.toString(securityUser.getId()))
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    /**
     * Получить ID пользователя из токена.
     *
     * @param token токен аутификации
     * @return ID пользователя
     */
    public Integer getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();

        return Integer.parseInt(claims.getSubject());
    }

    /**
     * Валидация токена.
     *
     * @param authToken токен аутификации
     * @return результат аутификации
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}

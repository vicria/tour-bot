package ar.vicria.security.microservice.configurations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class JwtAuthenticationResponse implements Serializable {

    private String accessToken;
    private String tokenType;


    public JwtAuthenticationResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }
}

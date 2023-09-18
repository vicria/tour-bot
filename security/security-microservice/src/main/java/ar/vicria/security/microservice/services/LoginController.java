package ar.vicria.security.microservice.services;

import ar.vicria.security.microservice.configurations.JwtAuthenticationResponse;
import ar.vicria.security.microservice.configurations.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

//    @Value("${security.token.type}")
    private String tokenTypePropValue = "Bearer";
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public LoginController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(),
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);


        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, tokenTypePropValue));
    }

    @GetMapping("/hi")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok("HI");
    }

    @GetMapping("/hi2")
    @PreAuthorize("hasAuthority('ADMIN2')")
    public ResponseEntity<?> testEndpoint2() {
        return ResponseEntity.ok("HI");
    }
}

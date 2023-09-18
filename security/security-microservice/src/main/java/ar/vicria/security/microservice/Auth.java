package ar.vicria.security.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Telegram adapter.
 */
@SpringBootApplication
public class Auth {
    /**
     * Start application.
     * @param args start.
     */
    public static void main(String[] args) {
        SpringApplication.run(Auth.class, args);
    }
}

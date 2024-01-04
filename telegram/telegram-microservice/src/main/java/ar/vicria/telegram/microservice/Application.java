package ar.vicria.telegram.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Telegram adapter.
 */
@SpringBootApplication
public class Application {
    /**
     * Start application.
     *
     * @param args start.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

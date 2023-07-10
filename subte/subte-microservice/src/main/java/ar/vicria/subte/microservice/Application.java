package ar.vicria.subte.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application subway.
 */
@SpringBootApplication(scanBasePackages = {"ar.vicria.subte.microservice", "ar.vicria.map.client"})
public class Application {
    /**
     * start.
     * @param args start
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

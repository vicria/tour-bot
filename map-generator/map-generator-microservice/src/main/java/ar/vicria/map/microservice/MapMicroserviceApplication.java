package ar.vicria.map.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application map-generator.
 */
@SpringBootApplication
public class MapMicroserviceApplication {

    /**
     * start.
     * @param args start
     */
    public static void main(String[] args) {
        SpringApplication.run(MapMicroserviceApplication.class, args);
    }
}
package ar.vicria.map.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Application map generator api.
 */
@SpringBootApplication
@EnableFeignClients
public class MapApiApplication {

    /**
     * start.
     * @param args start
     */
    public static void main(String[] args) {
        SpringApplication.run(MapApiApplication.class, args);
    }
}
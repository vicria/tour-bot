package ar.vicria.map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Application map generator api.
 */
@SpringBootApplication()
@EnableFeignClients
public class Application {

    /**
     * start.
     * @param args start
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
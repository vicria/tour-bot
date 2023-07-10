package ar.vicria.subte.microservice;

import ar.vicria.map.api.MapApiApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application subway.
 */
@SpringBootApplication(scanBasePackages = {"ar.vicria.subte.microservice", "ar.vicria.map.api"})
@ImportAutoConfiguration({MapApiApplication.class})
public class SubteApplication {

    /**
     * start.
     * @param args start
     */
    public static void main(String[] args) {
        SpringApplication.run(SubteApplication.class, args);
    }

}

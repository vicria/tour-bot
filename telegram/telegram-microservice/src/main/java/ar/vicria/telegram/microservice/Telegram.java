package ar.vicria.telegram.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


/**
 * Telegram adapter.
 */
@SpringBootApplication
public class Telegram {
    /**
     * Start application.
     * @param args start.
     */
    public static void main(String[] args) {
        SpringApplication.run(Telegram.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}

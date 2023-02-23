package ar.vicria.telegram.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {
    public static final String APPLICATION_NAME = "telegram";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

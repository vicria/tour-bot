package ar.vicria.tourbot.tourpoint.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {
    public static final String APPLICATION_NAME = "tour-point";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

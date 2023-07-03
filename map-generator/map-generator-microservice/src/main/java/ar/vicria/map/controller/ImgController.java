package ar.vicria.map.controller;

import ar.vicria.subte.dto.RouteDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class ImgController {

    @PostMapping (
            value = "/image",
            produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@RequestBody RouteDto routeDto) throws IOException {

        File file = new File("map-generator/map-generator-microservice/src/main/resources/images/map-subte.png");

        return Files.readAllBytes(file.toPath());
    }

}
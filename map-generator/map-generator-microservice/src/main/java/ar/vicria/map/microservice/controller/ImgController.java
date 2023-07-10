package ar.vicria.map.microservice.controller;

import ar.vicria.subte.dto.RouteDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Controller to receive a picture.
 */
@RestController
@RequestMapping("/api/image")
public class ImgController {

    /**
     * controller to get image.
     *
     * @param routeDto   RouteDto
     * @throws IOException for Files.readAllBytes
     * @return byte[] image
     */
    @PostMapping (
            value = "/generate")
    public byte[] getImage(@RequestBody RouteDto routeDto) throws IOException {

        File file = new File("map-generator/map-generator-microservice/src/main/resources/images/map-subte.png");

        return Files.readAllBytes(file.toPath());
    }

}
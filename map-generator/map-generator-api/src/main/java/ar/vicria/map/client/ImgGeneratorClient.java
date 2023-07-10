package ar.vicria.map.client;

import ar.vicria.subte.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client ImgController.
 */
@Component
@FeignClient(value = "mapGenerator", url = "http://localhost:8083/api/image")
public interface ImgGeneratorClient {

    /**
     * get request from ImgController.
     *
     * @param routeDto RouteDto
     * @return byte[]
     * */
    @PostMapping
    byte[] getImage(@RequestBody RouteDto routeDto);

}

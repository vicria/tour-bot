package ar.vicria.map.api.client;

import ar.vicria.subte.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client ImgController.
 */
@FeignClient(value = "subte-microservice", url = "http://localhost:8083/api/image/generate")
public interface ImgGeneratorClient {

    /**
     * get request from ImgController.
     *
     * @param routeDto RouteDto
     * @return byte[]
     * */
    @PostMapping()
    byte[] getImage(@RequestBody RouteDto routeDto);

}

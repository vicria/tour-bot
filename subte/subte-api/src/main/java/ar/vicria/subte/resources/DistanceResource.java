package ar.vicria.subte.resources;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Дистанции.
 */
public interface DistanceResource {

    /**
     * Рассчитать путь между станциями.
     * @param dto - направления
     * @return итоговый расчет
     */
    @PostMapping("/distance/count")
    @ResponseStatus(HttpStatus.OK)
    RouteDto count(DistanceDto dto);
}

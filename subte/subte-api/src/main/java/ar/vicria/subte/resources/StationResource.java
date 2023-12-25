package ar.vicria.subte.resources;

import ar.vicria.subte.dto.StationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Станции.
 */
@FeignClient(name = "stations", url = "${ar.vicria.adapter.telegram.subteUrl}")
@Component
public interface StationResource {

    /**
     * Получение информации о станции.
     *
     * @param id идентификатор станции
     * @return информация о станции
     */
    @GetMapping("/stations/{id}")
    StationDto getOne(@PathVariable("id") String id);

    /**
     * Получение информации о всех станциях.
     *
     * @return все станции
     */
    @GetMapping("/stations/all")
    List<StationDto> getAll();

    /**
     * Создание новой станции.
     *
     * @param dto форма станции
     * @return информация о станции
     */
    @PostMapping("/stations")
    StationDto create(@RequestBody StationDto dto);

    /**
     * Обновление станции.
     *
     * @param dto форма станции
     * @return информация о станции
     */
    @PatchMapping("/stations/")
    StationDto update(@RequestBody StationDto dto);
}

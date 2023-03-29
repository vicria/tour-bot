package ar.vicria.subte.resources;

import ar.vicria.subte.dto.StationDto;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Станции.
 */
public interface StationResource {
    
    /**
     * Получение информации о станции.
     *
     * @param id идентификатор станции
     * @return информация о станции
     */
    StationDto getOne(String id);

    /**
     *
     * @return
     */
    @GetMapping("/stations/all")
    List<StationDto> getAll();

    /**
     * Создание новой станции.
     *
     * @param dto форма станции
     * @return информация о станции
     */
    StationDto create(StationDto dto);

    /**
     * Обновление станции.
     *
     * @param id идентификатор станции
     * @param dto форма станции
     * @return информация о станции
     */
    StationDto update(String id, StationDto dto);

}

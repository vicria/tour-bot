package ar.vicria.telegram.microservice.stations;

import ar.vicria.subte.dto.StationDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Фабрика для сборки снимка каталога станций.
 */
@Component
public class StationCatalogFactory {

    /**
     * Сборка снимка загруженных станций.
     *
     * @param stationsByRest - станции из сервиса subte по get запросу
     * @return сформированный каталог станций
     */
    public StationCatalog create(List<StationDto> stationsByRest) {
        List<StationDto> stations = List.copyOf(stationsByRest);
        return new StationCatalog(stations, lines(stations), byLine(stations), byNameAndLine(stations));
    }

    /**
     * Уникальные линии станций.
     *
     * @param stations станции каталога
     * @return названия линий
     */
    public static List<String> lines(List<StationDto> stations) {
        return stations.stream()
                .map(StationDto::getLine)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Станции по линиям.
     *
     * @param stations станции каталога
     * @return станции по линиям
     */
    public static Map<String, List<StationDto>> byLine(List<StationDto> stations) {
        return Map.copyOf(stations.stream()
                .collect(Collectors.groupingBy(
                        StationDto::getLine, Collectors.toUnmodifiableList())));
    }

    /**
     * Станции по ключу название + линия.
     *
     * @param stations станции каталога
     * @return станции по ключу {@link StationDto#toString()} (название + линия)
     */
    public static Map<String, StationDto> byNameAndLine(List<StationDto> stations) {
        return stations.stream().collect(
                Collectors.toUnmodifiableMap(StationDto::toString, dto -> dto));
    }

}

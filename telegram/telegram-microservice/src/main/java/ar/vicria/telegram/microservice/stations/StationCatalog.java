package ar.vicria.telegram.microservice.stations;

import ar.vicria.subte.dto.StationDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Неизменяемый список станций.
 * Собирается фабрикой {@link StationCatalogFactory}
 */
@Getter
@RequiredArgsConstructor
public class StationCatalog {

    private final List<StationDto> stations;
    private final List<String> lines;
    private final Map<String, List<StationDto>> byLine;
    private final Map<String, StationDto> byNameAndLine;

}

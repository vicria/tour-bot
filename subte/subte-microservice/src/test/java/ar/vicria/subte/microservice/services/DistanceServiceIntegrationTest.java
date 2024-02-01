package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DistanceServiceIntegrationTest {

    @Autowired
    private DistanceService distanceService;
    @Autowired
    private StationService stationService;

    private static Stream<Arguments> testLastStationForLinesSource() {
        return Stream.of(
                Arguments.of("Juramento", "D🟢", "Callao", "B🔴", new StationDto("B🔴", "Leandro N. Alem")),
                Arguments.of("Callao", "B🔴", "Juramento", "D🟢", new StationDto("D🟢", "Congreso de Tucumán")),
                Arguments.of("San José", "E🟣", "Córdoba", "H🟡", new StationDto("H🟡", "Facultad de Derecho")),
                Arguments.of("Córdoba", "H🟡", "San José", "E🟣", new StationDto("E🟣", "Retiro")),
                Arguments.of("Lavalle", "C🔵", "San Juan", "C🔵", new StationDto("C🔵", "Constitución")),
                Arguments.of("San Juan", "C🔵", "Lavalle", "C🔵", new StationDto("C🔵", "Retiro")),
                Arguments.of("Carlos Gardel", "B🔴", "Loria", "A🌐", new StationDto("A🌐", "San Pedrito")),
                Arguments.of("Loria", "A🌐", "Carlos Gardel", "B🔴", new StationDto("B🔴", "Juan Manuel de Rosas Villa Urquiza"))
        );
    }

    @ParameterizedTest
    @MethodSource("testLastStationForLinesSource")
    void testLastStationForLines(String start, String startLine, String end, String endLine, StationDto lastStation) {
        StationDto routeStart = stationService.getByNameAndLine(start, startLine);
        StationDto routeEnd = stationService.getByNameAndLine(end, endLine);
        DistanceDto distanceDto = new DistanceDto();
        distanceDto.setFrom(routeStart);
        distanceDto.setTo(routeEnd);
        RouteDto count = distanceService.count(distanceDto);

        assertEquals(lastStation, count.getLastStation());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Leandro N. Alem                        |   B🔴  |Juan Manuel de Rosas Villa Urquiza  |B🔴|   17",
            "Juan Manuel de Rosas Villa Urquiza     |   B🔴  |Leandro N. Alem                     |B🔴|   17",
            "Facultad de Derecho                    |   H🟡  |Hospitales                          |H🟡|   12",
            "Hospitales                             |   H🟡  |Facultad de Derecho                 |H🟡|   12",
            "Congreso de Tucumán                    |   D🟢  |Catedral                            |D🟢|   16",
            "Catedral                               |   D🟢  |Congreso de Tucumán                 |D🟢|   16",
            "Constitución                           |   C🔵  |Retiro                              |C🔵|   9",
            "Retiro                                 |   C🔵  |Constitución                        |C🔵|   9",
            "San Pedrito                            |   A🌐  |Plaza de Mayo                       |A🌐|   18",
            "Plaza de Mayo                          |   A🌐  |San Pedrito                         |A🌐|   18",
            "Retiro                                 |   E🟣  |Plaza de los Virreyes               |E🟣|   18",
            "Plaza de los Virreyes                  |   E🟣  |Retiro                              |E🟣|   18",
    }, delimiter = '|')
    void testSizeForLines(String start, String startLine, String end, String endLine, int size) {
        var routeStart = stationService.getByNameAndLine(start, startLine);
        var routeEnd = stationService.getByNameAndLine(end, endLine);
        DistanceDto distanceDto = new DistanceDto();
        distanceDto.setFrom(routeStart);
        distanceDto.setTo(routeEnd);
        RouteDto count = distanceService.count(distanceDto);
        assertFalse(count.getRoute().isEmpty());
        assertEquals(size, count.getRoute().size());
    }

    @Test
    @Disabled("сейчас неправильно поведение")
    void testSizeForLines() {
        var routeStart = new StationDto();
        var routeEnd = new StationDto();
        DistanceDto distanceDto = new DistanceDto();
        distanceDto.setFrom(routeStart);
        distanceDto.setTo(routeEnd);
        RouteDto count = distanceService.count(distanceDto);
        assertTrue(count.getRoute().isEmpty());
    }
}
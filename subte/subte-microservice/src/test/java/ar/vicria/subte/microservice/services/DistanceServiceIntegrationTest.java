package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.RouteDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DistanceServiceIntegrationTest {

    @Autowired
    private DistanceService distanceService;
    @Autowired
    private StationService stationService;

    @ParameterizedTest
    @CsvSource({
            "Leandro N. Alem,B🔴,Juan Manuel de Rosas Villa Urquiza,B🔴,17",
            "Juan Manuel de Rosas Villa Urquiza,B🔴,Leandro N. Alem,B🔴,17",
            "Facultad de Derecho,H🟡,Hospitales,H🟡,12",
            "Hospitales,H🟡,Facultad de Derecho,H🟡,12",
            "Congreso de Tucumán,D🟢,Catedral,D🟢,16",
            "Catedral,D🟢,Congreso de Tucumán,D🟢,16",
    })
    void testSizeForLines(String start, String startLine, String end, String endLine, int size) {
        var routeStart = stationService.getByNameAndLine(start, startLine);
        var routeEnd = stationService.getByNameAndLine(end, endLine);
        List<RouteDto> route = distanceService.getRoute(routeStart, routeEnd);
        assertFalse(route.isEmpty());
        assertEquals(size, route.get(0).getRoute().size());
    }

}
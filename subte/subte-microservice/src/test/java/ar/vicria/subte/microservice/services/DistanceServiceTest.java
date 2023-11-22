package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DistanceServiceTest {

    @Mock
    ConnectionService connectionService;


    private static DistanceService service;

    @BeforeAll
    public static void setUp() {
        Map<StationDto, List<ConnectionDto>> stations = new HashMap<>();

        //Subway map for St. Peterburg Russia
        stations.put(new StationDto("blue", "Gorkovskaya"),
                Arrays.asList(new ConnectionDto(new StationDto("blue", "Nevskiy"),
                                new StationDto("blue", "Gorkovskaya"), 2.0,
                                new StationDto("blue", "Kupchino")),
                        new ConnectionDto(new StationDto("blue", "Petrogradskaya"),
                                new StationDto("", "2"), 30.0,
                                new StationDto("blue", "Parnas"))));

        stations.put(new StationDto("blue", "Nevskiy"),
                Arrays.asList(new ConnectionDto(new StationDto("blue", "Gorkovskaya"),
                                new StationDto("blue", "Nevskiy"), 2.0,
                                new StationDto("blue", "Parnas")),
                        new ConnectionDto(new StationDto("blue", "Sennaya"),
                                new StationDto("", "4"), 5.0,
                                new StationDto("blue", "Kupchino")),
                        new ConnectionDto(new StationDto("green", "Gostinniy dvor"),
                                new StationDto("", "Nevskiy"), 10.0,
                                new StationDto("", "Perehod"))));

        stations.put(new StationDto("green", "Gostinniy dvor"),
                Arrays.asList(
                        new ConnectionDto(new StationDto("green", "Vasiliostrovskaya"),
                                new StationDto("", "6"), 2.0,
                                new StationDto("green", "Begovaya")),
                        new ConnectionDto(new StationDto("blue", "Nevskiy"),
                                new StationDto("", "7"), 2.0,
                                new StationDto("", "Perehod")),
                        new ConnectionDto(new StationDto("green", "Mayakovskaya"),
                                new StationDto("", "8"), 5.0,
                                new StationDto("green", "Ribatskoe"))));

        stations.put(new StationDto("green", "Mayakovskaya"),
                Arrays.asList(
                        new ConnectionDto(new StationDto("green", "Gostinniy dvor"),
                                new StationDto("", "9"), 2.0,
                                new StationDto("green", "Begovaya")),
                        new ConnectionDto(new StationDto("red", "Vosstaniya"),
                                new StationDto("", "Nevskiy"), 5.0,
                                new StationDto("", "Perehod"))));

        stations.put(new StationDto("red", "Vosstaniya"),
                Arrays.asList(
                        new ConnectionDto(new StationDto("red", "Vladimirskaya"),
                                new StationDto("red", "Vosstaniya"), 2.0,
                                new StationDto("red", "Veterki")),
                        new ConnectionDto(new StationDto("red", "Chernishevskaya"),
                                new StationDto("", "12"), 2.0,
                                new StationDto("red", "Devyatkino")),
                        new ConnectionDto(new StationDto("green", "Mayakovskaya"),
                                new StationDto("", "13"), 10.0,
                                new StationDto("", "Perehod"))));

        stations.put(new StationDto("red", "Vladimirskaya"),
                Arrays.asList(new ConnectionDto(new StationDto("red", "Vosstaniya"),
                                new StationDto("", "14"), 2.0,
                                new StationDto("red", "Devyatkino")),
                        new ConnectionDto(new StationDto("orange", "Dostoyevskaya"),
                                new StationDto("", "15"), 10.0,
                                new StationDto("", "Perehod"))));

        stations.put(new StationDto("red", "Chernishevskaya"),
                Collections.singletonList(new ConnectionDto(new StationDto("red", "Vosstaniya"),
                        new StationDto("red", "Chernishevskaya"), 5.0,
                        new StationDto("red", "Veterki"))));

        stations.put(new StationDto("orange", "Dostoyevskaya"),
                Collections.singletonList(new ConnectionDto(new StationDto("orange", "Spasskaya"),
                        new StationDto("", "17"), 5.0,
                        new StationDto("orange", "Spasskaya"))));

        stations.put(new StationDto("orange", "Spasskaya"),
                Collections.singletonList(new ConnectionDto(new StationDto("blue", "Sennaya"),
                        new StationDto("", "18"), 10.0,
                        new StationDto("", "Perehod"))));

        stations.put(new StationDto("blue", "Sennaya"),
                Arrays.asList(new ConnectionDto(new StationDto("blue", "Nevskiy"),
                                new StationDto("", "19"), 2.0,
                                new StationDto("blue", "Parnas")),
                        new ConnectionDto(new StationDto("orange", "Spasskaya"),
                                new StationDto("", "20"), 5.0,
                                new StationDto("", "Perehod")),
                        new ConnectionDto(new StationDto("violate", "Sadovaya"),
                                new StationDto("", "21"), 10.0,
                                new StationDto("", "Perehod"))));

        service = new DistanceService(stations, null);
    }


    /**
     * Test from Nevskiy to Vasiliostrovskaya
     * expected:
     * One way with 10 min. Last station in distance is Begovaya
     */
    @Test
    public void testNevskiyToVasiliostrovskaya() {
        List<RouteDto> route = service.getRoute(new StationDto("blue", "Nevskiy"),
                new StationDto("green", "Vasiliostrovskaya"));

        int size = route.size();
        assertEquals(1, size);

        RouteDto routeDto = route.get(0);
        assertEquals(12, routeDto.getTotalTime());
        //todo last station must to change
        //        assertEquals("Begovaya", routeDto.getLastStation().getName());

        List<String> rout = new ArrayList<>();
        rout.add("Nevskiy");
        rout.add("Gostinniy dvor");
        rout.add("Vasiliostrovskaya");
        assertEquals(rout, routeDto.getRoute().stream().map(StationDto::getName).collect(Collectors.toList()));
    }

    /**
     * Test from Vosstaniya to Sennaya
     * expected:
     * Two ways
     */
    @Test
    public void testVostaniyaToSennaya() {
        List<RouteDto> route = service.getRoute(new StationDto("red", "Vosstaniya"),
                new StationDto("blue", "Sennaya"));

        int size = route.size();
        assertEquals(2, size);

        List<String> route1 = new ArrayList<>();
        route1.add("Vosstaniya");
        route1.add("Mayakovskaya");
        route1.add("Gostinniy dvor");
        route1.add("Nevskiy");
        route1.add("Sennaya");
        assertEquals(route1, route.get(0).getRoute().stream().map(StationDto::getName).collect(Collectors.toList()));

        //this is second cause has more minutes
        List<String> route2 = new ArrayList<>();
        route2.add("Vosstaniya");
        route2.add("Vladimirskaya");
        route2.add("Dostoyevskaya");
        route2.add("Spasskaya");
        route2.add("Sennaya");
        assertEquals(route2, route.get(1).getRoute().stream().map(StationDto::getName).collect(Collectors.toList()));
    }

    /**
     * Test from Gorkovskaya And Chernishevskaya
     * expected:
     * One direction will have
     */
    @Test
    public void testDirections() {
        List<RouteDto> route = service.getRoute(new StationDto("blue", "Gorkovskaya"),
                new StationDto("red", "Chernishevskaya"));

        int size = route.size();
        assertEquals(1, size);

        RouteDto routeDto = route.get(0);
        assertEquals("Devyatkino", routeDto.getLastStation().getName());

        List<RouteDto> route2 = service.getRoute(new StationDto("red", "Chernishevskaya"),
                new StationDto("blue", "Gorkovskaya"));

        int size2 = route2.size();
        assertEquals(1, size2);

        RouteDto routeDto2 = route2.get(0);
        assertEquals("Parnas", routeDto2.getLastStation().getName());
    }

    @Test
    public void firstStationIdTest(){

        DistanceDto dto = new DistanceDto();
        StationDto start = new StationDto("H\uD83D\uDFE1", "Facultad de Derecho");
        StationDto to = new StationDto("H\uD83D\uDFE1", "Las Heras");
        dto.setTo(to);
        dto.setFrom(start);


        Map<StationDto, List<ConnectionDto>> stations = new HashMap<>();
        StationDto toStation = new StationDto("H\uD83D\uDFE1", "Las Heras");
        StationDto fromStation = new StationDto("H\uD83D\uDFE1", "Facultad de Derecho");
        fromStation.setId("123");
        StationDto lastStation = new StationDto("H\uD83D\uDFE1", "Hospitales");
        ConnectionDto connectionDto1 = new ConnectionDto(toStation,
                fromStation, 1.1,
                lastStation);

        Mockito.when(connectionService.getAllAsDto()).thenReturn(List.of(connectionDto1));
        DistanceService distanceService = new DistanceService(stations, connectionService);
        var ansToCheck = distanceService.getRoute(dto.getFrom(), dto.getTo());

        Assertions.assertNotNull(ansToCheck.get(0).getRoute().get(0).getId());


    }
}
package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.dto.ConnectionDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class DistanceServiceTest {

    private DistanceService service;

    @Before
    public void setUp() {
        Map<String, List<ConnectionDto>> stations = new HashMap<>();
        //Subway map for St. Peterburg Russia
        stations.put("Gorkovskaya", Arrays.asList(
                new ConnectionDto(new StationDto("blue", "Nevskiy"), new StationDto("", ""), 2.0, new StationDto("blue", "Kupchino")),
                new ConnectionDto(new StationDto("blue", "Petrogradskaya"), new StationDto("", ""), 30.0, new StationDto("blue", "Parnas"))));
        stations.put("Nevskiy",
                Arrays.asList(new ConnectionDto(new StationDto("blue", "Gorkovskaya"), new StationDto("", ""), 2.0, new StationDto("blue", "Parnas")),
                        new ConnectionDto(new StationDto("blue", "Sennaya"), new StationDto("", ""), 5.0, new StationDto("blue", "Kupchino")),
                        new ConnectionDto(new StationDto("green", "Gostinniy dvor"), new StationDto("", ""), 10.0, new StationDto("", "Perehod"))));
        stations.put("Gostinniy dvor",
                Arrays.asList(
                        new ConnectionDto(new StationDto("green", "Vasiliostrovskaya"), new StationDto("", ""), 2.0, new StationDto("green", "Begovaya")),
                        new ConnectionDto(new StationDto("blue", "Nevskiy"), new StationDto("", ""), 2.0, new StationDto("", "Perehod")),
                        new ConnectionDto(new StationDto("green", "Mayakovskaya"), new StationDto("", ""), 5.0, new StationDto("green", "Ribatskoe"))));
        stations.put("Mayakovskaya",
                Arrays.asList(
                        new ConnectionDto(new StationDto("green", "Gostinniy dvor"), new StationDto("", ""), 2.0, new StationDto("green", "Begovaya")),
                        new ConnectionDto(new StationDto("red", "Vosstaniya"), new StationDto("", ""), 5.0, new StationDto("", "Perehod"))));
        stations.put("Vosstaniya",
                Arrays.asList(
                        new ConnectionDto(new StationDto("red", "Vladimirskaya"), new StationDto("", ""), 2.0, new StationDto("red", "Veterki")),
                        new ConnectionDto(new StationDto("red", "Chernishevskaya"), new StationDto("", ""), 2.0, new StationDto("red", "Devyatkino")),
                        new ConnectionDto(new StationDto("green", "Mayakovskaya"), new StationDto("", ""), 10.0, new StationDto("", "Perehod"))));
        stations.put("Vladimirskaya",
                Arrays.asList(new ConnectionDto(new StationDto("red", "Vosstaniya"), new StationDto("", ""), 2.0, new StationDto("red", "Devyatkino")),
                        new ConnectionDto(new StationDto("orange", "Dostoyevskaya"), new StationDto("", ""), 10.0, new StationDto("", "Perehod"))));
        stations.put("Chernishevskaya",
                Collections.singletonList(new ConnectionDto(new StationDto("red", "Vosstaniya"), new StationDto("", ""), 5.0, new StationDto("red", "Veterki"))));
        stations.put("Dostoyevskaya",
                Collections.singletonList(new ConnectionDto(new StationDto("orange", "Spasskaya"), new StationDto("", ""), 5.0, new StationDto("orange", "Spasskaya"))));
        stations.put("Spasskaya",
                Collections.singletonList(new ConnectionDto(new StationDto("blue", "Sennaya"), new StationDto("", ""), 10.0, new StationDto("", "Perehod"))));
        stations.put("Sennaya",
                Arrays.asList(new ConnectionDto(new StationDto("blue", "Nevskiy"), new StationDto("", ""), 2.0, new StationDto("blue", "Parnas")),
                        new ConnectionDto(new StationDto("orange", "Spasskaya"), new StationDto("", ""), 5.0, new StationDto("", "Perehod")),
                        new ConnectionDto(new StationDto("violate", "Sadovaya"), new StationDto("", ""), 10.0, new StationDto("", "Perehod"))));

        service = new DistanceService(stations, null);
    }


    /**
     * Test from Nevskiy to Vasiliostrovskaya
     * expected:
     * One way with 10 min. Last station in distance is Begovaya
     */
    @Test
    public void testNevskiyToVasiliostrovskaya() {
        List<RouteDto> route = service.getRoute("Nevskiy", "Vasiliostrovskaya");

        int size = route.size();
        assertEquals(1, size);

        RouteDto routeDto = route.get(0);
        assertEquals(12, routeDto.getTotalTime());
        assertEquals("Begovaya", routeDto.getLastStation());

        List<String> rout = new ArrayList<>();
        rout.add("Nevskiy");
        rout.add("Gostinniy dvor");
        rout.add("Vasiliostrovskaya");
        assertEquals(rout, routeDto.getRoute());
    }

    /**
     * Test from Vosstaniya to Sennaya
     * expected:
     * Two ways
     */
    @Test
    public void testVostaniyaToSennaya() {
        List<RouteDto> route = service.getRoute("Vosstaniya", "Sennaya");

        int size = route.size();
        assertEquals(2, size);

        List<String> route1 = new ArrayList<>();
        route1.add("Vosstaniya");
        route1.add("Mayakovskaya");
        route1.add("Gostinniy dvor");
        route1.add("Nevskiy");
        route1.add("Sennaya");
        assertEquals(route1, route.get(0).getRoute());

        //this is second cause has more minutes
        List<String> route2 = new ArrayList<>();
        route2.add("Vosstaniya");
        route2.add("Vladimirskaya");
        route2.add("Dostoyevskaya");
        route2.add("Spasskaya");
        route2.add("Sennaya");
        assertEquals(route2, route.get(1).getRoute());
    }

    /**
     * Test from Gorkovskaya And Chernishevskaya
     * expected:
     * One direction will have
     */
    @Test
    public void testDirections() {
        List<RouteDto> route = service.getRoute("Gorkovskaya", "Chernishevskaya");

        int size = route.size();
        assertEquals(1, size);

        RouteDto routeDto = route.get(0);
        assertEquals("Devyatkino", routeDto.getLastStation());

        List<RouteDto> route2 = service.getRoute("Chernishevskaya", "Gorkovskaya");

        int size2 = route2.size();
        assertEquals(1, size2);

        RouteDto routeDto2 = route2.get(0);
        assertEquals("Parnas", routeDto2.getLastStation());
    }
}
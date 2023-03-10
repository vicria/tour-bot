package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
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
        Map<String, List<Connection>> stations = new HashMap<>();
        //Subway map for St. Peterburg Russia
        stations.put("Gorkovskaya", Arrays.asList(
                new Connection(new StationDto("blue", "Nevskiy"), 2, "Kupchino"),
                new Connection(new StationDto("blue", "Petrogradskaya"), 30, "Parnas")));
        stations.put("Nevskiy",
                Arrays.asList(new Connection(new StationDto("blue", "Gorkovskaya"), 2, "Parnas"),
                        new Connection(new StationDto("blue", "Sennaya"), 5, "Kupchino"),
                        new Connection(new StationDto("green", "Gostinniy dvor"), 10, "Perehod")));
        stations.put("Gostinniy dvor",
                Arrays.asList(
                        new Connection(new StationDto("green", "Vasiliostrovskaya"), 2, "Begovaya"),
                        new Connection(new StationDto("blue", "Nevskiy"), 2, "Perehod"),
                        new Connection(new StationDto("green", "Mayakovskaya"), 5, "Ribatskoe")));
        stations.put("Mayakovskaya",
                Arrays.asList(
                        new Connection(new StationDto("green", "Gostinniy dvor"), 2, "Begovaya"),
                        new Connection(new StationDto("red", "Vosstaniya"), 5, "Perehod")));
        stations.put("Vosstaniya",
                Arrays.asList(
                        new Connection(new StationDto("red", "Vladimirskaya"), 2, "Veterki"),
                        new Connection(new StationDto("red", "Chernishevskaya"), 2, "Devyatkino"),
                        new Connection(new StationDto("green", "Mayakovskaya"), 10, "Perehod")));
        stations.put("Vladimirskaya",
                Arrays.asList(new Connection(new StationDto("red", "Vosstaniya"), 2, "Devyztkino"),
                        new Connection(new StationDto("orange", "Dostoyevskaya"), 10, "Perehod")));
        stations.put("Chernishevskaya",
                Collections.singletonList(new Connection(new StationDto("red", "Vosstaniya"), 5, "Veterki")));
        stations.put("Dostoyevskaya",
                Collections.singletonList(new Connection(new StationDto("orange", "Spasskaya"), 5, "Spasskaya")));
        stations.put("Spasskaya",
                Collections.singletonList(new Connection(new StationDto("blue", "Sennaya"), 10, "Perehod")));
        stations.put("Sennaya",
                Arrays.asList(new Connection(new StationDto("blue", "Nevskiy"), 2, "Parnas"),
                        new Connection(new StationDto("orange", "Spasskaya"), 5, "Perehod"),
                        new Connection(new StationDto("violate", "Sadovaya"), 10, "Perehod")));

        service = new DistanceService(stations);
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
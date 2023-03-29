package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.resources.DistanceResource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DistanceService implements DistanceResource {

    private Map<String, List<ConnectionDto>> stations;
    private StringBuilder lastic = new StringBuilder();
    private final ConnectionService service;

    @SneakyThrows
    @PostConstruct
    private void init() {
        stations = service.getAllAsDto().stream()
                .collect(Collectors.groupingBy(dto -> dto.getStationFrom().getName(), Collectors.toList()));
    }

    @Transactional
    public RouteDto count(DistanceDto dto) {
        List<RouteDto> route = getRoute(dto.getFrom(), dto.getTo());

        if (route.isEmpty()) {
            throw new IllegalArgumentException("Invalid input");
        } else {
            return route.get(0);
        }
    }

    public List<RouteDto> getRoute(String start, String end) {
        // Initialize visited and route taken lists
        Set<String> visited = new HashSet<>();
        List<List<String>> routes = new ArrayList<>();

        // Initialize priority queue with start station and priority 0
        PriorityQueue<RouteDto> queue = new PriorityQueue<>();
        List<String> initialRoute = new ArrayList<>();
        initialRoute.add(start);
        queue.offer(new RouteDto(initialRoute, 0, ""));

        while (!queue.isEmpty()) {
            // Get route with lowest priority (i.e. shortest so far)
            RouteDto shortestRoute = queue.poll();
            List<String> route = shortestRoute.getRoute();
            String lastStation = route.get(route.size() - 1);
            int totalTime = shortestRoute.getTotalTime();

            // Check if last station is the destination station
            if (lastStation.equals(end)) {
                routes.add(route);
                lastic = new StringBuilder();
                lastic.append(shortestRoute.getLastStation());
            }

            // Check if station has been visited already
            if (visited.contains(lastStation)) {
                continue;
            }

            // Add station to visited list
            visited.add(lastStation);

            // Add possible next stations to priority queue
            if (stations.get(lastStation) != null) {
                for (ConnectionDto connection : stations.get(lastStation)) {
                    String nextStation = connection.getStationTo().getName();
                    double travelTime = connection.getTravelTime();

                    if (!visited.contains(nextStation)) {
                        List<String> newRoute = new ArrayList<>(route);
                        newRoute.add(nextStation);
                        double newTotalTime = totalTime + travelTime;
                        //todo убрать int
                        queue.offer(new RouteDto(newRoute, (int) newTotalTime, connection.getLastStation().getName()));
                    }
                }
            }
        }

        return routes.stream()
                .map(rout -> {
                    int total1 = calculateTotalTime(rout);
                    return new RouteDto(rout, total1, lastic.toString());
                })
                .sorted(RouteDto::compareTo)
                .collect(Collectors.toList());

    }

    // Helper method to calculate the total time of a route
    private int calculateTotalTime(List<String> route) {
        int totalTime = 0;
        for (int i = 1; i < route.size(); i++) {
            String prevStation = route.get(i - 1);
            String currStation = route.get(i);
            for (ConnectionDto connection : stations.get(prevStation)) {
                if (connection.getStationTo().getName().equals(currStation)) {
                    totalTime += connection.getTravelTime();
                    break;
                }
            }
        }
        return totalTime;
    }

}

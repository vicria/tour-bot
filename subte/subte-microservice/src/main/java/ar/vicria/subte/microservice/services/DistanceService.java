package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.ConnectionDto;
import ar.vicria.subte.dto.DistanceDto;
import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.resources.DistanceResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * General service for counting rout.
 */
@Service
@Transactional
public class DistanceService implements DistanceResource {

    private Map<StationDto, List<ConnectionDto>> stations;

    /**
     * last station.
     */
    private StationDto lastic = new StationDto();

    /**
     * Constructor.
     *
     * @param stations          all stations in db. never changing without running.
     * @param connectionService all connections between stations.
     */
    public DistanceService(Map<StationDto, List<ConnectionDto>> stations, ConnectionService connectionService) {
        this.stations = stations;
        if (stations.size() == 0) {
            this.stations = connectionService.getAllAsDto().stream()
                    .collect(Collectors.groupingBy(ConnectionDto::getStationFrom, Collectors.toList()));
        }
    }

    /**
     * Counting rout.
     *
     * @param dto - rout
     * @return answer of counting.
     */
    @Transactional
    public RouteDto count(DistanceDto dto) {
        List<RouteDto> route = getRoute(dto.getFrom(), dto.getTo());

        if (route.isEmpty()) {
            throw new IllegalArgumentException("Invalid input");
        } else {
            return route.get(0);
        }
    }

    /**
     * Algoritm for routing.
     *
     * @param start first point
     * @param end   last point
     * @return time and stations in line
     */
    public List<RouteDto> getRoute(StationDto start, StationDto end) {
        // Initialize visited and route taken lists
        Set<StationDto> visited = new HashSet<>();
        List<RouteDto> routes = new ArrayList<>();

        List<ConnectionDto> transitionsList = new ArrayList<>();
        // Initialize priority queue with start station and priority 0
        PriorityQueue<RouteDto> queue = new PriorityQueue<>();
        List<StationDto> initialRoute = new ArrayList<>();
        initialRoute.add(start);
        queue.offer(new RouteDto(initialRoute, 0, lastic, new ArrayList<>()));


        while (!queue.isEmpty()) {
            // Get route with lowest priority (i.e. shortest so far)
            RouteDto shortestRoute = queue.poll();
            List<StationDto> route = shortestRoute.getRoute();
            StationDto lastStation = route.get(route.size() - 1);
            int totalTime = shortestRoute.getTotalTime();

            // Check if last station is the destination station
            if (lastStation.equals(end)) {
                routes.add(shortestRoute);
                continue;
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
                    StationDto nextStation = connection.getStationTo();
                    double travelTime = connection.getTravelTime();
                    if (!visited.contains(nextStation)) {
                        List<StationDto> newRoute = new ArrayList<>(route);
                        newRoute.add(nextStation);
                        double newTotalTime = totalTime + travelTime;
                        if (Optional.ofNullable(connection.getLastStation()).isPresent()
                                && !connection.getLastStation().getName().equals("Perehod")) {
                            lastic = connection.getLastStation();
                            queue.offer(new RouteDto(newRoute, (int) newTotalTime, lastic,
                                    shortestRoute.getTransitions().isEmpty() ? new ArrayList<>() : transitionsList));

                        } else if (connection.getLastStation() == null) {
                            lastic = shortestRoute.getLastStation();
                            var underpassConnection = new ConnectionDto(
                                    connection.getStationTo(),
                                    connection.getStationFrom(),
                                    connection.getTravelTime(),
                                    lastic);

                            transitionsList.add(underpassConnection);
                            queue.offer(new RouteDto(newRoute, (int) newTotalTime, lastic, transitionsList));
                        }
                    }
                }
            }
        }

        return routes.stream()
                .sorted(RouteDto::compareTo)
                .collect(Collectors.toList());

    }

}

package ar.vicria.subte.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Дто расчета итогового пути.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto implements Comparable<RouteDto> {

    private List<StationDto> route;
    private int totalTime;
    private List<Double> timeOfTransitions;
    private StationDto lastStation;

    @Override
    public int compareTo(RouteDto other) {
        return this.totalTime - other.totalTime;
    }
}
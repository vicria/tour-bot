package ar.vicria.subte.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Дто расчета итогового пути.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto implements Comparable<RouteDto> {

    private List<StationDto> route;
    private int totalTime;
    private StationDto lastStation;
    private List<ConnectionDto> transitions;

    @Override
    public int compareTo(RouteDto other) {
        return this.totalTime - other.totalTime;
    }
}
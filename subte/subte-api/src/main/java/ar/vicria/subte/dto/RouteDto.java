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

    private List<String> route;
    private int totalTime;
    private String lastStation;
    private Integer transition;

    @Override
    public int compareTo(RouteDto other) {
        return this.totalTime - other.totalTime;
    }
}
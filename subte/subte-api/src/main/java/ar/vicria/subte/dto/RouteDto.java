package ar.vicria.subte.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RouteDto implements Comparable<RouteDto> {
    public List<String> route;
    public int totalTime;
    public String lastStation;

    public int compareTo(RouteDto other) {
        return this.totalTime - other.totalTime;
    }
}
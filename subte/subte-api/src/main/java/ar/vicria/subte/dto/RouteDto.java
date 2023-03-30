package ar.vicria.subte.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto implements Comparable<RouteDto> {
    public List<String> route;
    public int totalTime;
    public String lastStation;
    public Integer transition;


    public int compareTo(RouteDto other) {
        return this.totalTime - other.totalTime;
    }
}
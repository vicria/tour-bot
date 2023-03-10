package ar.vicria.subte.microservice.services;

import lombok.Data;

import java.util.List;

@Data
public class Route implements Comparable<Route> {
    public List<String> route;
    public int totalTime;
    public String lastStation;

    public int compareTo(Route other) {
        return this.totalTime - other.totalTime;
    }
}
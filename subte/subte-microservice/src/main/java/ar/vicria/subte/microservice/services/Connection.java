package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.StationDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Connection {

    public StationDto station;

    public int travelTime;

    /**
     * Последняя.
     */
    private String lastStation;

}

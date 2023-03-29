package ar.vicria.subte.dto;

import ar.vicria.subte.dto.basic.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDto extends BaseDto {

    private StationDto stationTo;
    private StationDto stationFrom;

    private Double travelTime;

    /**
     * Последняя.
     */
    private StationDto lastStation;

}

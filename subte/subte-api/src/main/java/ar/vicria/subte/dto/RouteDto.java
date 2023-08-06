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

    /**
     * Конструктор.
     *
     * @param route       список станций
     * @param totalTime   затраченное время
     * @param lastStation последняя станция
     */
    public RouteDto(List<StationDto> route, int totalTime, StationDto lastStation) {
        this.route = route;
        this.totalTime = totalTime;
        this.lastStation = lastStation;
    }

    private List<StationDto> route;
    private int totalTime;
    private StationDto lastStation;

    private String chatId;
    private Integer msgId;
    private String clazzName;

    @Override
    public int compareTo(RouteDto other) {
        return this.totalTime - other.totalTime;
    }
}
package ar.vicria.subte.dto;

import ar.vicria.subte.dto.basic.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ДТО направления.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DistanceDto extends BaseDto {

    /**
     * Станция ОТ.
     */
    private StationDto from;

    /**
     * Станция ДО.
     */
    private StationDto to;

    private String chatId;
    private Integer msgId;
    private String clazzName;

}

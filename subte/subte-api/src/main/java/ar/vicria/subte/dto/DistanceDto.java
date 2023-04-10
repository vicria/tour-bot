package ar.vicria.subte.dto;

import ar.vicria.subte.dto.basic.BaseDto;
import lombok.Data;

/**
 * ДТО направления.
 */
@Data
public class DistanceDto extends BaseDto {

    /**
     * Станция ОТ.
     */
    private String from;

    /**
     * Станция ДО.
     */
    private String to;

}

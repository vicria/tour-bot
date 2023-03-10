package ar.vicria.subte.dto;

import ar.vicria.subte.dto.basic.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ДТО направления
 */
@Data
@AllArgsConstructor
public class StationDto extends BaseDto {

    /**
     * Линия.
     */
    private String line;

    /**
     * Станция.
     */
    private String name;
}

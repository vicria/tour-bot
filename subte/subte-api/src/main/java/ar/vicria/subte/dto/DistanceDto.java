package ar.vicria.subte.dto;

import ar.vicria.subte.dto.basic.BaseDto;
import ar.vicria.subte.dto.validators.NonNullId;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * ДТО направления.
 */
@Data
@Validated
public class DistanceDto extends BaseDto {

    /**
     * Станция ОТ.
     */

    @NonNullId
    private StationDto from;

    /**
     * Станция ДО.
     */

    @NonNullId
    private StationDto to;

}

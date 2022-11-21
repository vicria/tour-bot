package ar.vicria.tourbot.tourpoint.dto;

import ar.vicria.tourbot.tourpoint.dto.basic.BaseDto;
import lombok.Data;

/**
 * ДТО сертификата
 */
@Data
public class PointDto extends BaseDto {

    /**
     * Номер.
     */
    private String index;

    /**
     * Номер.
     */
    private String question;
}

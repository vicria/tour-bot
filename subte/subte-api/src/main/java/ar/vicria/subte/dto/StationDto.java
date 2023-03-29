package ar.vicria.subte.dto;

import ar.vicria.subte.dto.basic.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * ДТО направления
 */
@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class StationDto extends BaseDto {

    /**
     * Линия.
     */
    @NotBlank
    private String line;

    /**
     * Станция.
     */
    @NotBlank
    private String name;
}

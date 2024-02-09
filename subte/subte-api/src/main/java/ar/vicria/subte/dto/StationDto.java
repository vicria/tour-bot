package ar.vicria.subte.dto;

import ar.vicria.subte.dto.basic.BaseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;

/**
 * ДТО направления.
 */
@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StationDto extends BaseDto {

    /**
     * Линия.
     */
    @NotBlank(message = "{required}")
    private String line;

    /**
     * Станция.
     */
    @NotBlank(message = "{required}")
    private String name;

    @Override
    public String toString() {
        return String.join(" ", Arrays.asList(name, line));
    }
}

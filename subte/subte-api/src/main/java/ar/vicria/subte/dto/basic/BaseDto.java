package ar.vicria.subte.dto.basic;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * Base dto.
 */
@Data
public abstract class BaseDto {

    /**
     * key.
     */
    private String id;
}

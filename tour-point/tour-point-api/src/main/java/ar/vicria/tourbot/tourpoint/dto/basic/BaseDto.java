package ar.vicria.tourbot.tourpoint.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto {

    @Nullable
    private boolean isNew = true;

    /**
     * Дата создания.
     */
    private ZonedDateTime createdAt;

    /**
     * Время последней модификации сущности.
     */
    private ZonedDateTime lastModifiedAt;
}

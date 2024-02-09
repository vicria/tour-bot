package ar.vicria.subte.microservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Дто ошибок.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    private OffsetDateTime dateTime = OffsetDateTime.now();
    private String message;
    @Nullable
    private String uuid = UUID.randomUUID().toString();
    @Nullable
    private String stackTrace;

    /**
     * Конструктор.
     *
     * @param message - сообщение ошибки
     */
    public ErrorDto(String message) {
        this.message = message;
    }

    /**
     * Конструктор.
     *
     * @param message    - сообщение ошибки
     * @param stackTrace stackTrace
     */
    public ErrorDto(String message, @Nullable String stackTrace) {
        this.message = message;
        this.stackTrace = stackTrace;
    }

}

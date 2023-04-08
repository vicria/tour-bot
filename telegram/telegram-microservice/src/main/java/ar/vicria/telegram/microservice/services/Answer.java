package ar.vicria.telegram.microservice.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Ответ
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    private String text;
    private Integer code;
}
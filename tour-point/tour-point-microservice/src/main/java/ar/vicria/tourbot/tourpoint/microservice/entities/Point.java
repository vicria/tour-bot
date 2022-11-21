package ar.vicria.tourbot.tourpoint.microservice.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Типы обучений.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldNameConstants
@Entity
public class Point extends BaseEntity {

    /**
     * Номер.
     */
    @Column
    private String index;

    /**
     * Тип обучения.
     */
    @Column
    private String question;

}

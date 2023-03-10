package ar.vicria.subte.microservice.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Станция.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldNameConstants
@Entity
public class Station extends BaseEntity {

    /**
     * Номер.
     */
    @Column
    private String name;

    /**
     * Линия.
     */
    @Column
    private String line;

}

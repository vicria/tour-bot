package ar.vicria.subte.microservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;


/**
 * Станция.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldNameConstants
@Entity(name = "subte_station")
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

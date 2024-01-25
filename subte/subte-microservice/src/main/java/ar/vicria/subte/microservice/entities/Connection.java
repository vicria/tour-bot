package ar.vicria.subte.microservice.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "subte_connection")
public class Connection extends BaseEntity {

    /**
     * Station start.
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Station stationFrom;

    /**
     * Station stop.
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Station stationTo;

    /**
     * Time for driving.
     */
    @Column
    public Double travelTime;

    /**
     * Последняя станция направления.
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Station lastStation;

}

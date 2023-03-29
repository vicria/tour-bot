package ar.vicria.subte.microservice.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

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
     * Станция.
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Station stationFrom;

    /**
     * Станция.
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Station stationTo;

    @Column
    public Double travelTime;

    /**
     * Последняя станция направления.
     */
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Station lastStation;

}

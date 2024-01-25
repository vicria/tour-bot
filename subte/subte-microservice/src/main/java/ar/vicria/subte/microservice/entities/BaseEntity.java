package ar.vicria.subte.microservice.entities;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

/**
 * Base Entity.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
@FieldNameConstants
public abstract class BaseEntity implements Serializable {

    /**
     * primary key.
     */
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

}

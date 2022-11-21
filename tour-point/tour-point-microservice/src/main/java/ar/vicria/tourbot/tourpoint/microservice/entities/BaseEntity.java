package ar.vicria.tourbot.tourpoint.microservice.entities;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
@FieldNameConstants
public abstract class BaseEntity implements Serializable {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Version
    @Builder.Default
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Long ts = -1L;

    /**
     * Дата создания.
     */
    @Getter
    @Setter
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    /**
     * Время последней модификации сущности.
     */
    @Getter
    @Setter
    @Column(nullable = false)
    private ZonedDateTime lastModifiedAt;

    public Long getVersion() {
        return ts;
    }

    public void setVersion(final Long ts) {
        this.ts = ts;
    }
}

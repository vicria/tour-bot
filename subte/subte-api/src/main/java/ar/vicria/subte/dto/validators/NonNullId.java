package ar.vicria.subte.dto.validators;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Verifies if a field has a non-null id.
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNullId {
    /**
     * Default message.
     * @return "ID must be not null"
     */
    String message() default "ID must be not null";

    /**
     * Default groups.
     * @return null.
     */
    Class<?>[] groups() default {};

    /**
     * Default payload.
     * @return null.
     */
    Class<? extends Payload>[] payload() default {};
}

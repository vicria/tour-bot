package ar.vicria.kafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * KafkaProperties has 2 notBlank fields.
 */
class KafkaPropertiesTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final List<String> required = Arrays.asList("roadMessageTopic", "subteRoadTopic");

    @Test
    public void testValidationEn() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        var properties = new KafkaProperties();
        properties.setRoadMessageTopic("topic1");
        properties.setSubteRoadTopic("topic2");
        var errors = validator.validate(properties);
        Assertions.assertEquals(0, errors.size(), "Without errors");

        properties = new KafkaProperties();
        errors = validator.validate(properties);
        ConstraintViolation[] constraintViolations = errors.toArray(ConstraintViolation[]::new);
        for (ConstraintViolation violation : constraintViolations) {
            Assertions.assertEquals("must not be blank", violation.getMessage());
            Assertions.assertTrue(required.contains(violation.getPropertyPath().toString()));
        }
    }
}
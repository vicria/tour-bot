package ar.vicria.telegram.microservice.services.validators;

import ar.vicria.telegram.microservice.properties.TelegramProperties;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * TelegramProperties has 4 notBlank fields.
 */
public class TelegramPropertiesValidatorTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final List<String> required = Arrays.asList("botToken", "botUserName", "subteGet");

    @Test
    public void testEn() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        var properties = new TelegramProperties();
        properties.setBotToken("token");
        properties.setBotUserName("name");
        properties.setSubteGet("get");
        var errors = validator.validate(properties);
        Assertions.assertEquals(0, errors.size(), "Without errors");

        properties = new TelegramProperties();
        errors = validator.validate(properties);
        ConstraintViolation[] constraintViolations = errors.toArray(ConstraintViolation[]::new);
        for (ConstraintViolation violation : constraintViolations) {
            Assertions.assertEquals("must not be blank", violation.getMessage());
            Assertions.assertTrue(required.contains(violation.getPropertyPath().toString()));
        }
    }
}

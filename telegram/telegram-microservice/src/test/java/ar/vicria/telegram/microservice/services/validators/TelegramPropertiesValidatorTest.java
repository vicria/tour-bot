package ar.vicria.telegram.microservice.services.validators;

import ar.vicria.telegram.microservice.properties.TelegramProperties;
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
 * TelegramProperties has 2 notBlank fields.
 */
public class TelegramPropertiesValidatorTest {

    private final List<String> required = Arrays.asList("botToken", "botUserName");

    @Test
    public void testEn() {
        Locale.setDefault(Locale.ENGLISH);
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        var properties = new TelegramProperties();
        properties.setBotToken("token");
        properties.setBotUserName("name");
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

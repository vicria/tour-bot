package ar.vicria.subte.microservice.services;

import ar.vicria.subte.dto.StationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

class DistanceDtoValidatorTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test()
    public void testIdNonNull(){
        StationDto from = new StationDto();
        from.setId("1");
        StationDto to = new StationDto();
        to.setId("2");
        var errorsFrom = validator.validate(from);
        var errorsTo = validator.validate(to);
        Assertions.assertEquals(0, errorsFrom.size());
        Assertions.assertEquals(0, errorsTo.size());

        from = new StationDto();
        to = new StationDto();
        errorsFrom = validator.validate(from);
        errorsTo = validator.validate(to);
        ConstraintViolation[] constraintViolationsFrom = errorsFrom.toArray(ConstraintViolation[]::new);
        for (ConstraintViolation violation : constraintViolationsFrom) {
            Assertions.assertEquals("ID must be not null", violation.getMessage());
        }

        ConstraintViolation[] constraintViolationsTo = errorsTo.toArray(ConstraintViolation[]::new);
        for (ConstraintViolation violation : constraintViolationsTo) {
            Assertions.assertEquals("ID must be not null", violation.getMessage());
        }
    }

}
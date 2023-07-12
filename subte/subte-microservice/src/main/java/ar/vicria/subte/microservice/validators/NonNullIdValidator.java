package ar.vicria.subte.microservice.validators;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.subte.dto.validators.NonNullId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for the NonNullIdAnnotation.
 */
public class NonNullIdValidator implements ConstraintValidator<NonNullId, StationDto> {

    @Override
    public void initialize(NonNullId constraintAnnotation) {

    }

    @Override
    public boolean isValid(StationDto stationDto, ConstraintValidatorContext constraintValidatorContext) {
        return stationDto != null && stationDto.getId() != null;
    }

}

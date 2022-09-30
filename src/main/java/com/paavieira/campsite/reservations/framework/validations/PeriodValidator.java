package com.paavieira.campsite.reservations.framework.validations;

import com.paavieira.campsite.reservations.framework.controllers.dtos.FindAvailability;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PeriodValidator implements ConstraintValidator<ValidPeriod, FindAvailability> {

    @Override
    public void initialize(ValidPeriod constraintAnnotation) {
    }

    @Override
    public boolean isValid(FindAvailability findAvailability, ConstraintValidatorContext context) {
        return (findAvailability == null) ? true : findAvailability.isValidPeriod();
    }
}

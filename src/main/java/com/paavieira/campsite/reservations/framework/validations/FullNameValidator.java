package com.paavieira.campsite.reservations.framework.validations;

import com.paavieira.campsite.reservations.framework.controllers.dtos.User;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FullNameValidator implements ConstraintValidator<ValidFullName, User> {

    @Override
    public void initialize(ValidFullName constraintAnnotation) {
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext cxt) {
        return (user == null) ? true : StringUtils.hasLength(user.getFirstName()) && StringUtils.hasLength(user.getLastName());
    }

}
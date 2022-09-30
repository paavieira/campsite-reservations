package com.paavieira.campsite.reservations.framework.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FullNameValidator.class)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFullName {
    String message() default "First name and last name are both required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
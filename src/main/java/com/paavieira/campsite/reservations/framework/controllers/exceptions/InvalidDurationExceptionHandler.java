package com.paavieira.campsite.reservations.framework.controllers.exceptions;

import com.paavieira.campsite.reservations.domain.exceptions.InvalidDuration;
import com.paavieira.campsite.reservations.framework.controllers.dtos.ResponseError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class InvalidDurationExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(InvalidDuration.class)
    public ResponseError invalidDuration(InvalidDuration e) {
        return new ResponseError(BAD_REQUEST.value(), e.getMessage());
    }
}

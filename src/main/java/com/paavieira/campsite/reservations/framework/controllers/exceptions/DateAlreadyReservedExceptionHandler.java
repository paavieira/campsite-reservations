package com.paavieira.campsite.reservations.framework.controllers.exceptions;

import com.paavieira.campsite.reservations.domain.exceptions.DateAlreadyReserved;
import com.paavieira.campsite.reservations.framework.controllers.dtos.ResponseError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
public class DateAlreadyReservedExceptionHandler {

    @ResponseStatus(CONFLICT)
    @ResponseBody
    @ExceptionHandler(DateAlreadyReserved.class)
    public ResponseError dateAlreadyReserved(DateAlreadyReserved e) {
        return new ResponseError(CONFLICT.value(), e.getMessage());
    }
}

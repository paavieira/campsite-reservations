package com.paavieira.campsite.reservations.framework.controllers.exceptions;

import com.paavieira.campsite.reservations.domain.exceptions.ReservationNotFound;
import com.paavieira.campsite.reservations.framework.controllers.dtos.ResponseError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ReservationNotFoundExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(ReservationNotFound.class)
    public ResponseError reservationNotFound(ReservationNotFound e) {
        return new ResponseError(NOT_FOUND.value(), e.getMessage());
    }
}

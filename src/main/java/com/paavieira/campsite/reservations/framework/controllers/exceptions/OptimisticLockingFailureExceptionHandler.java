package com.paavieira.campsite.reservations.framework.controllers.exceptions;

import com.paavieira.campsite.reservations.framework.controllers.dtos.ResponseError;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

public class OptimisticLockingFailureExceptionHandler {

    @ResponseStatus(CONFLICT)
    @ResponseBody
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseError dateAlreadyReserved(OptimisticLockingFailureException e) {
        return new ResponseError(CONFLICT.value(), e.getMessage());
    }

}

package com.paavieira.campsite.reservations.framework.controllers.exceptions;

import com.paavieira.campsite.reservations.framework.controllers.dtos.ResponseError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseError methodArgumentNotValidException(MethodArgumentNotValidException ex) {

        final BindingResult result = ex.getBindingResult();

        final int globalErrorCount = result.getGlobalErrorCount();
        final List<String> globalErrors = result.getGlobalErrors().stream()
                                                .map(ge -> ge.getDefaultMessage()).collect(toList());

        final int fieldErrorCount = result.getFieldErrorCount();
        final Map<String, List<String>> fieldErrors = result.getFieldErrors().stream()
                                                            .collect(groupingBy(FieldError::getField,
                                                                                mapping(FieldError::getDefaultMessage, toList())));

        return new ResponseError(BAD_REQUEST.value(), globalErrorCount, globalErrors, fieldErrorCount, fieldErrors);
    }

}

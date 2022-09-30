package com.paavieira.campsite.reservations.framework.controllers.dtos;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ResponseError {

    private final int status;

    private final int globalErrorCount;
    private final List<String> globalErrors;

    private final int fieldErrorCount;
    private final Map<String, List<String>> fieldErrors;

    public ResponseError(int status, String globalError) {
        this(status, 1, Collections.singletonList(globalError), 0, Collections.emptyMap());
    }

    public ResponseError(
            int status,
            int globalErrorCount,
            List<String> globalErrors,
            int fieldErrorCount,
            Map<String, List<String>> fieldErrors) {
        this.status = status;
        this.globalErrorCount = globalErrorCount;
        this.globalErrors = globalErrors;
        this.fieldErrorCount = fieldErrorCount;
        this.fieldErrors = fieldErrors;
    }

    public int getStatus() {
        return status;
    }

    public int getGlobalErrorCount() {
        return globalErrorCount;
    }

    public List<String> getGlobalErrors() {
        return globalErrors;
    }

    public int getFieldErrorCount() {
        return fieldErrorCount;
    }

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }

}
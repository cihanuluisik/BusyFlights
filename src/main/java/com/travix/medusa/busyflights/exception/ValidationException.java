package com.travix.medusa.busyflights.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException {

    private List<FieldException> fieldErrors = new ArrayList<>();

    public ValidationException() {
    }

    public void addFieldError(String path, String message) {
        FieldException error = new FieldException(path, message);
        fieldErrors.add(error);
    }

    public List<FieldException> getFieldErrors() {
        return fieldErrors;
    }

}
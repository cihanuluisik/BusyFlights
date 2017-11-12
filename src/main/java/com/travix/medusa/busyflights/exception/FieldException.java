package com.travix.medusa.busyflights.exception;

public class FieldException {
 
    private String field;
 
    private String message;
 
    public FieldException(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}
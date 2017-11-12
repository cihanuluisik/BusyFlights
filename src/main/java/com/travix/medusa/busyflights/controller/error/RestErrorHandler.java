package com.travix.medusa.busyflights.controller.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {


    private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
        ValidationError validationError = new ValidationError();

        for (FieldError fieldError: fieldErrors) {
            validationError.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return validationError;
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
                                                         HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return new ResponseEntity(processFieldErrors(fieldErrors), headers, HttpStatus.BAD_REQUEST);
    }


}
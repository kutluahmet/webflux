package com.textkernel.javatask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomCVException extends RuntimeException {

    public CustomCVException(String message) {
        super(message);
    }
}

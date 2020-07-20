package com.textkernel.javatask.exception;

import lombok.Value;

import java.util.Date;

/**
 * @author AKUTLU
 * Generic Error Template
 */
@Value
public class ErrorDetails {
    private final Date timestamp;
    private final String message;
    private final String details;


    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}

package com.jwtly10.loggar.exceptions;

public class JobException extends RuntimeException {
    private String message;

    public JobException(String message) {
        super(message);
        this.message = message;
    }

    public String getExceptionMessage() {
        return this.message;
    }
}

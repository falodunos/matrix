package com.league.test.matrix.exceptions;

public class InvalidMatrixException extends RuntimeException {
    public InvalidMatrixException(String message) {
        super(message);
    }

    public InvalidMatrixException(String message, Throwable cause) {
        super(message, cause);
    }
}
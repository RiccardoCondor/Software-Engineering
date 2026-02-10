package org.example.exceptions;

public class FarmacoNonDisponibileException extends RuntimeException {
    public FarmacoNonDisponibileException(String message) {
        super(message);
    }
}

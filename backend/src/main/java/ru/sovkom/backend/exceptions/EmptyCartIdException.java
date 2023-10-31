package ru.sovkom.backend.exceptions;

public class EmptyCartIdException extends RuntimeException {
    public EmptyCartIdException(String message) {
        super(message);
    }
}


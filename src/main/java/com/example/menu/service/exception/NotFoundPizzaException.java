package com.example.menu.service.exception;

/**
 * @author Patryk Borchowiec
 */
public class NotFoundPizzaException extends RuntimeException {
    public NotFoundPizzaException(String message) {
        super(message);
    }
}

package com.example.menu.service.exception;

/**
 * @author Patryk Borchowiec
 */
public class PizzaAlreadyExistsException extends RuntimeException {
    public PizzaAlreadyExistsException(String message) {
        super(message);
    }
}

package com.example.menu.service.interfaces;

import com.example.menu.service.dto.PizzaDto;
import com.example.menu.service.exception.NotFoundPizzaException;
import com.example.menu.service.exception.PizzaAlreadyExistsException;
import lombok.NonNull;

import java.util.List;

/**
 * @author Patryk Borchowiec
 */
public interface PizzaService {
    void createPizza(@NonNull final PizzaDto pizzaDto) throws PizzaAlreadyExistsException;
    List<PizzaDto> getAllPizzas();
    PizzaDto getPizzaById(@NonNull final Long id) throws NotFoundPizzaException;
    void updatePizza(@NonNull final PizzaDto pizzaDto) throws NotFoundPizzaException, PizzaAlreadyExistsException;
    void deletePizzaById(@NonNull final Long id) throws NotFoundPizzaException;
}

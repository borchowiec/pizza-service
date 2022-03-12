package com.example.menu.api.v1.controller;

import com.example.menu.api.v1.pojo.converter.PizzaV1Converter;
import com.example.menu.api.v1.pojo.request.CreatePizzaV1Request;
import com.example.menu.api.v1.pojo.request.UpdatePizzaV1Request;
import com.example.menu.api.v1.pojo.response.PizzaV1Response;
import com.example.menu.service.dto.PizzaDto;
import com.example.menu.service.exception.NotFoundPizzaException;
import com.example.menu.service.exception.PizzaAlreadyExistsException;
import com.example.menu.service.interfaces.PizzaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Patryk Borchowiec
 */
@RestController
@RequestMapping(path = "/api/v1/pizzas", produces = MediaType.APPLICATION_JSON_VALUE)
public class PizzaV1Controller {
    private final PizzaV1Converter pizzaV1Converter;
    private final PizzaService pizzaService;

    public PizzaV1Controller(final PizzaV1Converter pizzaV1Converter, final PizzaService pizzaService) {
        this.pizzaV1Converter = pizzaV1Converter;
        this.pizzaService = pizzaService;
    }

    @PostMapping
    public void createPizza(@Valid @RequestBody final CreatePizzaV1Request request) {
        final PizzaDto pizzaDto = pizzaV1Converter.toDto(request);
        try {
            pizzaService.createPizza(pizzaDto);
        } catch (final PizzaAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot create pizza due to: " + e.getMessage());
        }
    }

    @GetMapping
    public List<PizzaV1Response> getAllPizzas() {
        return pizzaService.getAllPizzas().stream()
                .map(pizzaV1Converter::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{pizzaId}")
    public PizzaV1Response getPizzaById(@PathVariable final long pizzaId) {
        try {
            return pizzaV1Converter.toResponse(pizzaService.getPizzaById(pizzaId));
        } catch (final NotFoundPizzaException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot get pizza by id due to: " + e.getMessage());
        }
    }

    @PutMapping("/{pizzaId}")
    public void updatePizza(@Valid @RequestBody final UpdatePizzaV1Request request, @PathVariable final long pizzaId) {
        try {
            pizzaService.updatePizza(pizzaV1Converter.toDto(request, pizzaId));
        } catch (final NotFoundPizzaException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cannot update pizza by id due to: " + e.getMessage()
            );
        } catch (final PizzaAlreadyExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot update pizza by id due to: " + e.getMessage()
            );
        }
    }

    @DeleteMapping("/{pizzaId}")
    public void deletePizzaById(@PathVariable final Long pizzaId) {
        try {
            pizzaService.deletePizzaById(pizzaId);
        } catch (final NotFoundPizzaException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cannot delete pizza by id due to: " + e.getMessage()
            );
        }
    }
}

package com.example.menu.api.v1.pojo.converter;

import com.example.menu.api.v1.pojo.request.CreatePizzaV1Request;
import com.example.menu.api.v1.pojo.request.UpdatePizzaV1Request;
import com.example.menu.api.v1.pojo.response.PizzaV1Response;
import com.example.menu.service.dto.PizzaDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Patryk Borchowiec
 */
@Component
public class PizzaV1Converter {
    public PizzaDto toDto(final CreatePizzaV1Request request) {
        if (Objects.isNull(request)) {
            return null;
        }

        return new PizzaDto(
                null,
                request.getName(),
                request.getPrice(),
                request.getIngredients()
        );
    }

    public PizzaV1Response toResponse(final PizzaDto pizzaDto) {
        if (Objects.isNull(pizzaDto)) {
            return null;
        }

        return new PizzaV1Response(
                pizzaDto.getId(),
                pizzaDto.getName(),
                pizzaDto.getPrice(),
                pizzaDto.getIngredients()
        );
    }

    public PizzaDto toDto(final UpdatePizzaV1Request request, final long pizzaId) {
        if (Objects.isNull(request)) {
            return null;
        }

        return new PizzaDto(
                pizzaId,
                request.getName(),
                request.getPrice(),
                request.getIngredients()
        );
    }
}

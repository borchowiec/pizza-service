package com.example.menu.service.dto.converter;

import com.example.menu.repository.entity.IngredientEntity;
import com.example.menu.repository.entity.PizzaEntity;
import com.example.menu.service.dto.PizzaDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Patryk Borchowiec
 */
@Component
public class PizzaConverter {
    private final IngredientConverter ingredientConverter;

    public PizzaConverter(IngredientConverter ingredientConverter) {
        this.ingredientConverter = ingredientConverter;
    }

    public PizzaEntity toNewEntity(final PizzaDto pizzaDto) {
        if (Objects.isNull(pizzaDto)) {
            return null;
        }

        final List<String> ingredients = Optional.ofNullable(pizzaDto.getIngredients())
                .orElse(List.of());
        final List<IngredientEntity> ingredientEntities = ingredients.stream()
                .map(ingredientConverter::toEntity)
                .collect(Collectors.toList());

        final PizzaEntity pizzaEntity = new PizzaEntity();
        pizzaEntity.setIngredients(ingredientEntities);
        pizzaEntity.setName(pizzaDto.getName());
        pizzaEntity.setPrice(pizzaDto.getPrice());

        return pizzaEntity;
    }

    public PizzaEntity toEntity(final PizzaDto pizzaDto) {
        if (Objects.isNull(pizzaDto)) {
            return null;
        }

        final List<String> ingredients = Optional.ofNullable(pizzaDto.getIngredients())
                .orElse(List.of());
        final List<IngredientEntity> ingredientEntities = ingredients.stream()
                .map(ingredientConverter::toEntity)
                .collect(Collectors.toList());

        final PizzaEntity pizzaEntity = new PizzaEntity();
        pizzaEntity.setId(pizzaDto.getId());
        pizzaEntity.setIngredients(ingredientEntities);
        pizzaEntity.setName(pizzaDto.getName());
        pizzaEntity.setPrice(pizzaDto.getPrice());

        return pizzaEntity;
    }

    public PizzaDto toDto(final PizzaEntity pizzaEntity) {
        if (Objects.isNull(pizzaEntity)) {
            return null;
        }

        final List<IngredientEntity> ingredients = Optional.ofNullable(pizzaEntity.getIngredients())
                .orElse(List.of());

        return new PizzaDto(
                pizzaEntity.getId(),
                pizzaEntity.getName(),
                pizzaEntity.getPrice(),
                ingredients.stream().map(IngredientEntity::getName).collect(Collectors.toList())
        );
    }
}

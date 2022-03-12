package com.example.menu.test.utils;

import com.example.menu.api.v1.pojo.request.CreatePizzaV1Request;
import com.example.menu.api.v1.pojo.request.UpdatePizzaV1Request;
import com.example.menu.repository.entity.IngredientEntity;
import com.example.menu.repository.entity.PizzaEntity;
import com.example.menu.service.dto.PizzaDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * @author Patryk Borchowiec
 */
public class ObjectsUtils {
    public static PizzaDto createPizzaDto(Map<String, Object> args) {
        args = Optional.ofNullable(args).orElse(Map.of());

        return new PizzaDto(
                (Long) args.getOrDefault("id", Math.abs(new Random().nextLong())),
                (String) args.getOrDefault("name", "Margherita " + new Random().nextInt(1000)),
                (Double) args.getOrDefault("price", 20.00),
                (List<String>) args.getOrDefault("ingredients", List.of())
        );
    }

    public static PizzaEntity createPizzaEntity(Map<String, Object> args) {
        args = Optional.ofNullable(args).orElse(Map.of());

        final PizzaEntity pizzaEntity = new PizzaEntity();
        pizzaEntity.setId((Long) args.getOrDefault("id", Math.abs(new Random().nextLong())));
        pizzaEntity.setName((String) args.getOrDefault("name", "Margherita " + new Random().nextInt(1000)));
        pizzaEntity.setPrice((Double) args.getOrDefault("price", 20.00));
        pizzaEntity.setIngredients(List.of());

        return pizzaEntity;
    }

    public static IngredientEntity createIngredientEntity(Map<String, Object> args) {
        args = Optional.ofNullable(args).orElse(Map.of());

        final IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setId((Long) args.getOrDefault("id", Math.abs(new Random().nextLong())));
        ingredientEntity.setName((String) args.getOrDefault("name", "Ingredient " + new Random().nextInt(1000)));

        return ingredientEntity;
    }

    public static CreatePizzaV1Request createCreatePizzaV1Request(Map<String, Object> args) {
        args = Optional.ofNullable(args).orElse(Map.of());

        return new CreatePizzaV1Request(
                (String) args.getOrDefault("name", "Margheritta " + new Random().nextInt(1000)),
                (Double) args.getOrDefault("price", 20.00),
                (List<String>) args.getOrDefault("ingredients", List.of())
        );
    }

    public static UpdatePizzaV1Request createUpdatePizzaV1Request(Map<String, Object> args) {
        args = Optional.ofNullable(args).orElse(Map.of());

        return new UpdatePizzaV1Request(
                (String) args.getOrDefault("name", "Margheritta " + new Random().nextInt(1000)),
                (Double) args.getOrDefault("price", 20.00),
                (List<String>) args.getOrDefault("ingredients", List.of())
        );
    }
}

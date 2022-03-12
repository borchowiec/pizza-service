package com.example.menu.service.dto.converter;

import com.example.menu.repository.entity.IngredientEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Patryk Borchowiec
 */
class IngredientConverterTest {

    private final IngredientConverter ingredientConverter = new IngredientConverter();

    @Test
    void toEntity() {
        // given
        final String ingredient = "Cheese";

        // when
        final IngredientEntity actual = ingredientConverter.toEntity(ingredient);

        // then
        final IngredientEntity expected = new IngredientEntity();
        expected.setName(ingredient);
        assertEquals(expected, actual);
    }
}
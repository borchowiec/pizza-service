package com.example.menu.service.dto.converter;

import com.example.menu.repository.entity.IngredientEntity;
import org.springframework.stereotype.Component;

/**
 * @author Patryk Borchowiec
 */
@Component
public class IngredientConverter {
    public IngredientEntity toEntity(final String ingredient) {
        final IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setName(ingredient);
        return ingredientEntity;
    }
}

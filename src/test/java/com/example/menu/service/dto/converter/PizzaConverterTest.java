package com.example.menu.service.dto.converter;

import com.example.menu.repository.entity.IngredientEntity;
import com.example.menu.repository.entity.PizzaEntity;
import com.example.menu.service.dto.PizzaDto;
import com.example.menu.test.utils.ObjectsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Patryk Borchowiec
 */
class PizzaConverterTest {
    @InjectMocks
    private PizzaConverter pizzaConverter;

    @Mock
    private IngredientConverter ingredientConverter;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class toNewEntity {
        @Test
        void passedNullValue_shouldReturnNull() {
            // given
            final PizzaDto dto = null;
            final PizzaEntity expected = null;

            // when
            final PizzaEntity actual = pizzaConverter.toNewEntity(dto);

            // then
            assertEquals(expected, actual);
        }

        @Test
        void passedNonNullValue_shouldReturnDtoObject() {
            // given
            final PizzaDto dto = new PizzaDto(
                    10L, "Margherita", 20.99, List.of("Cheese", "Sauce")
            );
            final List<IngredientEntity> ingredients = dto.getIngredients().stream().map(ingredient -> {
                        final IngredientEntity ingredientEntity = new IngredientEntity();
                        ingredientEntity.setName(ingredient);
                        return ingredientEntity;
                    })
                    .collect(Collectors.toList());
            final PizzaEntity expected = new PizzaEntity();
            expected.setIngredients(ingredients);
            expected.setPrice(dto.getPrice());
            expected.setName(dto.getName());

            // when
            for (int i = 0; i < dto.getIngredients().size(); i++) {
                when(ingredientConverter.toEntity(dto.getIngredients().get(i))).thenReturn(ingredients.get(i));
            }
            final PizzaEntity actual = pizzaConverter.toNewEntity(dto);

            // then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class toEntity {
        @Test
        void passedNullValue_shouldReturnNull() {
            // given
            final PizzaDto dto = null;
            final PizzaEntity expected = null;

            // when
            final PizzaEntity actual = pizzaConverter.toEntity(dto);

            // then
            assertEquals(expected, actual);
        }

        @Test
        void passedNonNullValue_shouldReturnDtoObject() {
            // given
            final PizzaDto dto = new PizzaDto(
                    10L, "Margherita", 20.99, List.of("Cheese", "Sauce")
            );
            final List<IngredientEntity> ingredients = dto.getIngredients().stream().map(ingredient -> {
                        final IngredientEntity ingredientEntity = new IngredientEntity();
                        ingredientEntity.setName(ingredient);
                        return ingredientEntity;
                    })
                    .collect(Collectors.toList());
            final PizzaEntity expected = new PizzaEntity();
            expected.setId(dto.getId());
            expected.setIngredients(ingredients);
            expected.setPrice(dto.getPrice());
            expected.setName(dto.getName());

            // when
            for (int i = 0; i < dto.getIngredients().size(); i++) {
                when(ingredientConverter.toEntity(dto.getIngredients().get(i))).thenReturn(ingredients.get(i));
            }
            final PizzaEntity actual = pizzaConverter.toEntity(dto);

            // then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class toDto {
        @Test
        void passedNullValue_shouldReturnNull() {
            // given
            final PizzaEntity entity = null;
            final PizzaDto expected = null;

            // when
            final PizzaDto actual = pizzaConverter.toDto(entity);

            // then
            assertEquals(expected, actual);
        }

        @Test
        void passedNonNullValue_shouldReturnDtoObject() {
            // given
            final PizzaEntity entity = new PizzaEntity();
            entity.setId(10L);
            entity.setName("Margherita");
            entity.setPrice(20.99);
            entity.setIngredients(List.of(ObjectsUtils.createIngredientEntity(null), ObjectsUtils.createIngredientEntity(null)));

            final PizzaDto expected = new PizzaDto(
                    entity.getId(),
                    entity.getName(),
                    entity.getPrice(),
                    entity.getIngredients().stream().map(IngredientEntity::getName).collect(Collectors.toList())
            );

            // when
            final PizzaDto actual = pizzaConverter.toDto(entity);

            // then
            assertEquals(expected, actual);
        }
    }
}
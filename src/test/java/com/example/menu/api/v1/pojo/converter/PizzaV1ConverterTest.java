package com.example.menu.api.v1.pojo.converter;

import com.example.menu.api.v1.pojo.request.CreatePizzaV1Request;
import com.example.menu.api.v1.pojo.request.UpdatePizzaV1Request;
import com.example.menu.api.v1.pojo.response.PizzaV1Response;
import com.example.menu.service.dto.PizzaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Patryk Borchowiec
 */
class PizzaV1ConverterTest {
    private PizzaV1Converter pizzaV1Converter;

    @BeforeEach
    public void beforeEach() {
        pizzaV1Converter = new PizzaV1Converter();
    }

    @Nested
    class toDto {
        @Nested
        class withCreatePizzaV1Request {
            @Test
            void passedNullValue_shouldReturnNull() {
                // given
                final CreatePizzaV1Request request = null;
                final PizzaDto expected = null;

                // when
                final PizzaDto actual = pizzaV1Converter.toDto(request);

                // then
                assertEquals(expected, actual);
            }

            @Test
            void passedNonNullValue_shouldReturnDtoObject() {
                // given
                final CreatePizzaV1Request request = new CreatePizzaV1Request(
                        "Margherita", 20.99, List.of("Cheese", "Sauce")
                );
                final PizzaDto expected = new PizzaDto(
                        null, request.getName(), request.getPrice(), request.getIngredients()
                );

                // when
                final PizzaDto actual = pizzaV1Converter.toDto(request);

                // then
                assertEquals(expected, actual);
            }
        }

        @Nested
        class withUpdatePizzaV1Request {
            @Test
            void passedNullValue_shouldReturnNull() {
                // given
                final UpdatePizzaV1Request request = null;
                final long id = 19L;
                final PizzaDto expected = null;

                // when
                final PizzaDto actual = pizzaV1Converter.toDto(request, id);

                // then
                assertEquals(expected, actual);
            }

            @Test
            void passedNonNullValue_shouldReturnDtoObject() {
                // given
                final long id = 19L;
                final UpdatePizzaV1Request request = new UpdatePizzaV1Request(
                        "Margherita", 20.99, List.of("Cheese", "Sauce")
                );
                final PizzaDto expected = new PizzaDto(
                        id, request.getName(), request.getPrice(), request.getIngredients()
                );

                // when
                final PizzaDto actual = pizzaV1Converter.toDto(request, 19L);

                // then
                assertEquals(expected, actual);
            }
        }
    }

    @Nested
    class toResponse {
        @Test
        void passedNullValue_shouldReturnNull() {
            // given
            final PizzaDto dto = null;
            final PizzaV1Response expected = null;

            // when
            final PizzaV1Response actual = pizzaV1Converter.toResponse(dto);

            // then
            assertEquals(expected, actual);
        }

        @Test
        void passedNonNullValue_shouldReturnResponseObject() {
            // given
            final PizzaDto dto = new PizzaDto(
                    12L, "Margherita", 20.99, List.of("Cheese", "Sauce")
            );
            final PizzaV1Response expected = new PizzaV1Response(
                dto.getId(), dto.getName(), dto.getPrice(), dto.getIngredients()
            );

            // when
            final PizzaV1Response actual = pizzaV1Converter.toResponse(dto);

            // then
            assertEquals(expected, actual);
        }
    }
}
package com.example.menu.api.v1.controller;

import com.example.menu.api.v1.pojo.converter.PizzaV1Converter;
import com.example.menu.api.v1.pojo.request.CreatePizzaV1Request;
import com.example.menu.api.v1.pojo.request.UpdatePizzaV1Request;
import com.example.menu.api.v1.pojo.response.PizzaV1Response;
import com.example.menu.service.dto.PizzaDto;
import com.example.menu.service.exception.NotFoundPizzaException;
import com.example.menu.service.exception.PizzaAlreadyExistsException;
import com.example.menu.service.interfaces.PizzaService;
import com.example.menu.test.utils.ObjectsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Patryk Borchowiec
 */
@WebMvcTest(controllers = PizzaV1Controller.class)
class PizzaV1ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PizzaV1Converter pizzaV1Converter;

    @MockBean
    private PizzaService pizzaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    class createPizza {
        @Test
        void wrongRequest_shouldReturn400() throws Exception {
            final CreatePizzaV1Request request = ObjectsUtils.createCreatePizzaV1Request(Map.of("name", ""));

            sendPost(request).andExpect(status().isBadRequest());
        }

        @Test
        void serviceThrowsPizzaAlreadyExistsException_shouldReturn409() throws Exception {
            // given
            final CreatePizzaV1Request request = ObjectsUtils.createCreatePizzaV1Request(null);
            final PizzaDto pizzaDto = new PizzaDto(
                    null, request.getName(), request.getPrice(), request.getIngredients()
            );

            // when
            when(pizzaV1Converter.toDto(request)).thenReturn(pizzaDto);
            doThrow(PizzaAlreadyExistsException.class).when(pizzaService).createPizza(any(PizzaDto.class));

            // then
            sendPost(request).andExpect(status().isConflict());
        }

        @Test
        void serviceThrowsNothing_shouldReturn200() throws Exception {
            // given
            final CreatePizzaV1Request request = ObjectsUtils.createCreatePizzaV1Request(null);
            final PizzaDto pizzaDto = new PizzaDto(
                    null, request.getName(), request.getPrice(), request.getIngredients()
            );

            // when
            when(pizzaV1Converter.toDto(request)).thenReturn(pizzaDto);
            doNothing().when(pizzaService).createPizza(pizzaDto);

            // then
            sendPost(request).andExpect(status().isOk());
        }
    }

    @Test
    void getAllPizzas() throws Exception {
        // given
        final List<PizzaDto> pizzaDtoList = List.of(
                ObjectsUtils.createPizzaDto(null),
                ObjectsUtils.createPizzaDto(null),
                ObjectsUtils.createPizzaDto(null)
        );
        final List<PizzaV1Response> expected = pizzaDtoList.stream().map(pizzaDto -> new PizzaV1Response(
                        pizzaDto.getId(),
                        pizzaDto.getName(),
                        pizzaDto.getPrice(),
                        pizzaDto.getIngredients())
                )
                .collect(Collectors.toList());

        // when
        when(pizzaService.getAllPizzas()).thenReturn(pizzaDtoList);
        for (int i = 0; i < pizzaDtoList.size(); i++) {
            when(pizzaV1Converter.toResponse(pizzaDtoList.get(i))).thenReturn(expected.get(i));
        }

        // then
        final ResultActions resultActions = sendGet("");
        resultActions.andExpect(status().isOk());
        final String stringResponse = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(expected), stringResponse);
    }

    @Nested
    class getPizzaById {
        @Test
        void serviceThrowsNotFoundPizzaException_shouldReturn404() throws Exception {
            // given
            final long pizzaId = 10L;

            // when
            when(pizzaService.getPizzaById(pizzaId)).thenThrow(NotFoundPizzaException.class);

            // then
            sendGet("/" + pizzaId).andExpect(status().isNotFound());
        }

        @Test
        void serviceThrowsNothing_shouldReturn200AndPizza() throws Exception {
            // given
            final PizzaDto pizzaDto = ObjectsUtils.createPizzaDto(null);
            final PizzaV1Response expected = new PizzaV1Response(
                    pizzaDto.getId(),
                    pizzaDto.getName(),
                    pizzaDto.getPrice(),
                    pizzaDto.getIngredients()
            );

            // when
            when(pizzaService.getPizzaById(pizzaDto.getId())).thenReturn(pizzaDto);
            when(pizzaV1Converter.toResponse(pizzaDto)).thenReturn(expected);

            // then
            final ResultActions resultActions = sendGet("/" + pizzaDto.getId());
            resultActions.andExpect(status().isOk());
            final String stringResponse = resultActions.andReturn().getResponse().getContentAsString();
            assertEquals(objectMapper.writeValueAsString(expected), stringResponse);
        }
    }

    @Nested
    class updatePizza {
        @Test
        void serviceThrowsNotFoundPizzaException_shouldReturn404() throws Exception {
            // given
            final long pizzaId = 10L;
            final UpdatePizzaV1Request request = ObjectsUtils.createUpdatePizzaV1Request(null);
            final PizzaDto pizzaDto = new PizzaDto(
                    pizzaId, request.getName(), request.getPrice(), request.getIngredients()
            );

            // when
            when(pizzaV1Converter.toDto(request, pizzaId)).thenReturn(pizzaDto);
            doThrow(NotFoundPizzaException.class).when(pizzaService).updatePizza(pizzaDto);

            // then
            sendPut("/" + pizzaId, request).andExpect(status().isNotFound());
        }

        @Test
        void serviceThrowsPizzaAlreadyExistsException_shouldReturn409() throws Exception {
            // given
            final long pizzaId = 10L;
            final UpdatePizzaV1Request request = ObjectsUtils.createUpdatePizzaV1Request(null);
            final PizzaDto pizzaDto = new PizzaDto(
                    pizzaId, request.getName(), request.getPrice(), request.getIngredients()
            );

            // when
            when(pizzaV1Converter.toDto(request, pizzaId)).thenReturn(pizzaDto);
            doThrow(PizzaAlreadyExistsException.class).when(pizzaService).updatePizza(pizzaDto);

            // then
            sendPut("/" + pizzaId, request).andExpect(status().isConflict());
        }

        @Test
        void serviceThrowsNothing_shouldReturn200() throws Exception {
            // given
            final long pizzaId = 10L;
            final UpdatePizzaV1Request request = ObjectsUtils.createUpdatePizzaV1Request(null);
            final PizzaDto pizzaDto = new PizzaDto(
                    pizzaId, request.getName(), request.getPrice(), request.getIngredients()
            );

            // when
            when(pizzaV1Converter.toDto(request, pizzaId)).thenReturn(pizzaDto);
            doNothing().when(pizzaService).updatePizza(pizzaDto);

            // then
            sendPut("/" + pizzaId, request).andExpect(status().isOk());
        }
    }

    @Nested
    class deletePizzaById {
        @Test
        void serviceThrowsNotFoundPizzaException_shouldReturn404() throws Exception {
            // given
            final long pizzaId = 10L;

            // when
            doThrow(NotFoundPizzaException.class).when(pizzaService).deletePizzaById(pizzaId);

            // then
            sendDelete("/" + pizzaId).andExpect(status().isNotFound());
        }

        @Test
        void serviceThrowsNothing_shouldReturn200() throws Exception {
            // given
            final long pizzaId = 10L;

            // when
            doNothing().when(pizzaService).deletePizzaById(pizzaId);

            // then
            sendDelete("/" + pizzaId).andExpect(status().isOk());
        }
    }

    private ResultActions sendPost(Object request) throws Exception {
        return mockMvc.perform(
                post("/api/v1/pizzas")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions sendGet(String appendUrl) throws Exception {
        return mockMvc.perform(
                get("/api/v1/pizzas" + appendUrl)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions sendPut(String appendUrl, Object request) throws Exception {
        return mockMvc.perform(
                put("/api/v1/pizzas" + appendUrl)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions sendDelete(String appendUrl) throws Exception {
        return mockMvc.perform(
                delete("/api/v1/pizzas" + appendUrl)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }
}
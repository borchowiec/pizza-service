package com.example.menu.service.impl;

import com.example.menu.repository.entity.IngredientEntity;
import com.example.menu.repository.entity.PizzaEntity;
import com.example.menu.repository.interfaces.PizzaRepository;
import com.example.menu.service.dto.PizzaDto;
import com.example.menu.service.dto.converter.PizzaConverter;
import com.example.menu.service.exception.NotFoundPizzaException;
import com.example.menu.service.exception.PizzaAlreadyExistsException;
import com.example.menu.test.utils.ObjectsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Patryk Borchowiec
 */
class PizzaServiceImplTest {
    @InjectMocks
    private PizzaServiceImpl pizzaService;

    @Mock
    private PizzaRepository pizzaRepository;

    @Mock
    private PizzaConverter pizzaConverter;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class createPizza {
        @Test
        void pizzaWithGivenNameAlreadyExists_shouldThrowPizzaAlreadyExistsException() {
            // given
            final PizzaDto pizzaDto = ObjectsUtils.createPizzaDto(Map.of("id", 0L));

            // when
            when(pizzaRepository.existsByName(pizzaDto.getName())).thenReturn(true);

            // then
            assertThrows(
                    PizzaAlreadyExistsException.class,
                    () -> pizzaService.createPizza(pizzaDto),
                    String.format("Pizza with name '%s' already exists.", pizzaDto.getName())
            );
        }

        @Test
        void properData_shouldSavePizza() {
            // given
            final PizzaDto pizzaDto = ObjectsUtils.createPizzaDto(Map.of("id", 0L));
            final PizzaEntity pizzaEntity = ObjectsUtils.createPizzaEntity(Map.of(
                    "id", 0L,
                    "name", pizzaDto.getName(),
                    "price", pizzaDto.getPrice(),
                    "ingredients", pizzaDto.getIngredients()
            ));

            // when
            when(pizzaRepository.existsByName(pizzaDto.getName())).thenReturn(false);
            when(pizzaConverter.toNewEntity(pizzaDto)).thenReturn(pizzaEntity);
            pizzaService.createPizza(pizzaDto);

            // then
            verify(pizzaRepository, times(1)).save(pizzaEntity);
        }
    }

    @Nested
    class getAllPizzas {
        @Test
        void thereAreNoPizzas_shouldReturnEmptyList() {
            // given
            final List<PizzaEntity> pizzas = List.of();
            final List<PizzaDto> expected = List.of();

            // when
            when(pizzaRepository.findAll()).thenReturn(pizzas);
            List<PizzaDto> actual = pizzaService.getAllPizzas();

            // then
            assertEquals(expected, actual);
        }

        @Test
        void thereArePizzas_shouldReturnListOfPizzas() {
            // given
            final List<PizzaEntity> pizzas = List.of(
                    ObjectsUtils.createPizzaEntity(null),
                    ObjectsUtils.createPizzaEntity(null)
            );
            final List<PizzaDto> expected = pizzas.stream()
                    .map(pizzaEntity -> new PizzaDto(
                            pizzaEntity.getId(),
                            pizzaEntity.getName(),
                            pizzaEntity.getPrice(),
                            pizzaEntity.getIngredients().stream().map(IngredientEntity::getName).collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());

            // when
            when(pizzaRepository.findAll()).thenReturn(pizzas);
            for (int i = 0; i < pizzas.size(); i++) {
                when(pizzaConverter.toDto(pizzas.get(i))).thenReturn(expected.get(i));
            }
            List<PizzaDto> actual = pizzaService.getAllPizzas();

            // then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class getPizzaById {
        @Test
        void pizzaDoesNotExist_shouldThrowNotFoundPizzaException() {
            // given
            final Long id = 10L;

            // when
            when(pizzaRepository.findById(id)).thenReturn(Optional.empty());

            // then
            assertThrows(
                    NotFoundPizzaException.class,
                    () -> pizzaService.getPizzaById(id),
                    String.format("Not found pizza of id '%d'.", id)
            );
        }

        @Test
        void pizzaExist_shouldReturnPizza() {
            // given
            final PizzaEntity pizzaEntity = ObjectsUtils.createPizzaEntity(null);
            final PizzaDto expected = ObjectsUtils.createPizzaDto(Map.of(
                    "id", pizzaEntity.getId(),
                    "name", pizzaEntity.getName(),
                    "price", pizzaEntity.getPrice(),
                    "ingredients", pizzaEntity.getIngredients()
            ));

            // when
            when(pizzaRepository.findById(pizzaEntity.getId())).thenReturn(Optional.of(pizzaEntity));
            when(pizzaConverter.toDto(pizzaEntity)).thenReturn(expected);
            final PizzaDto actual = pizzaService.getPizzaById(pizzaEntity.getId());

            // then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class updatePizza {
        @Test
        void pizzaDoesNotExist_shouldThrowNotFoundPizzaException() {
            // given
            final PizzaDto pizzaDto = ObjectsUtils.createPizzaDto(null);

            // when
            when(pizzaRepository.findById(pizzaDto.getId())).thenReturn(Optional.empty());

            // then
            assertThrows(
                    NotFoundPizzaException.class,
                    () -> pizzaService.updatePizza(pizzaDto),
                    String.format("Not found pizza of id '%d'.", pizzaDto.getId())
            );
        }

        @Test
        void pizzaWithGivenNameAlreadyExists_shouldThrowPizzaAlreadyExistsException() {
            // given
            final PizzaDto pizzaDto = ObjectsUtils.createPizzaDto(null);
            final PizzaEntity pizzaEntity = ObjectsUtils.createPizzaEntity(Map.of(
                    "id", pizzaDto.getId()
            ));

            // when
            when(pizzaRepository.findById(pizzaDto.getId())).thenReturn(Optional.of(pizzaEntity));
            when(pizzaRepository.existsByName(pizzaDto.getName())).thenReturn(true);

            // then
            assertThrows(
                    PizzaAlreadyExistsException.class,
                    () -> pizzaService.updatePizza(pizzaDto),
                    String.format("Pizza with name '%s' already exists.", pizzaDto.getName())
            );
        }

        @Test
        void notUpdatingOldPizzaName_shouldUpdatePizza() {
            // given
            final PizzaDto pizzaDto = ObjectsUtils.createPizzaDto(null);
            final PizzaEntity oldPizzaEntity = ObjectsUtils.createPizzaEntity(Map.of(
                    "id", pizzaDto.getId(),
                    "name", pizzaDto.getName()
            ));
            final PizzaEntity newPizzaEntity = ObjectsUtils.createPizzaEntity(Map.of(
                    "id", pizzaDto.getId(),
                    "name", pizzaDto.getName(),
                    "price", pizzaDto.getPrice(),
                    "ingredients", pizzaDto.getIngredients()
            ));

            // when
            when(pizzaRepository.findById(pizzaDto.getId())).thenReturn(Optional.of(oldPizzaEntity));
            when(pizzaConverter.toEntity(pizzaDto)).thenReturn(newPizzaEntity);
            pizzaService.updatePizza(pizzaDto);

            // then
            verify(pizzaRepository, times(1)).save(newPizzaEntity);
        }

        @Test
        void updatingOldPizzaName_shouldUpdatePizza() {
            // given
            final PizzaDto pizzaDto = ObjectsUtils.createPizzaDto(null);
            final PizzaEntity oldPizzaEntity = ObjectsUtils.createPizzaEntity(Map.of(
                    "id", pizzaDto.getId()
            ));
            final PizzaEntity newPizzaEntity = ObjectsUtils.createPizzaEntity(Map.of(
                    "id", pizzaDto.getId(),
                    "name", pizzaDto.getName(),
                    "price", pizzaDto.getPrice(),
                    "ingredients", pizzaDto.getIngredients()
            ));

            // when
            when(pizzaRepository.findById(pizzaDto.getId())).thenReturn(Optional.of(oldPizzaEntity));
            when(pizzaConverter.toEntity(pizzaDto)).thenReturn(newPizzaEntity);
            pizzaService.updatePizza(pizzaDto);

            // then
            verify(pizzaRepository, times(1)).save(newPizzaEntity);
        }
    }

    @Nested
    class deletePizzaById {
        @Test
        void pizzaDoesNotExist_shouldThrowNotFoundPizzaException() {
            // given
            final Long id = 10L;

            // when
            when(pizzaRepository.existsById(id)).thenReturn(false);

            // then
            assertThrows(
                    NotFoundPizzaException.class,
                    () -> pizzaService.deletePizzaById(id),
                    String.format("Not found pizza of id '%d'.", id)
            );
        }

        @Test
        void pizzaExist_shouldReturnPizza() {
            // given
            final Long id = 10L;

            // when
            when(pizzaRepository.existsById(id)).thenReturn(true);
            pizzaService.deletePizzaById(id);

            // then
            verify(pizzaRepository, times(1)).deleteById(id);
        }
    }
}
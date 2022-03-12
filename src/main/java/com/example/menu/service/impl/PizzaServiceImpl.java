package com.example.menu.service.impl;

import com.example.menu.repository.entity.PizzaEntity;
import com.example.menu.repository.interfaces.PizzaRepository;
import com.example.menu.service.dto.PizzaDto;
import com.example.menu.service.dto.converter.PizzaConverter;
import com.example.menu.service.exception.NotFoundPizzaException;
import com.example.menu.service.exception.PizzaAlreadyExistsException;
import com.example.menu.service.interfaces.PizzaService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Patryk Borchowiec
 */
@Service
public class PizzaServiceImpl implements PizzaService {
    private final PizzaRepository pizzaRepository;
    private final PizzaConverter pizzaConverter;

    public PizzaServiceImpl(final PizzaRepository pizzaRepository, PizzaConverter pizzaConverter) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaConverter = pizzaConverter;
    }

    @Override
    public void createPizza(@NonNull final PizzaDto pizzaDto) throws PizzaAlreadyExistsException {
        if (pizzaRepository.existsByName(pizzaDto.getName())) {
            throw new PizzaAlreadyExistsException(
                    String.format("Pizza with name '%s' already exists.", pizzaDto.getName())
            );
        }

        final PizzaEntity pizzaEntity = pizzaConverter.toNewEntity(pizzaDto);
        pizzaRepository.save(pizzaEntity);
    }

    @Override
    public List<PizzaDto> getAllPizzas() {
        return pizzaRepository.findAll().stream()
                .map(pizzaConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PizzaDto getPizzaById(@NonNull final Long id) throws NotFoundPizzaException {
        return pizzaRepository.findById(id)
                .map(pizzaConverter::toDto)
                .orElseThrow(() -> new NotFoundPizzaException(String.format("Not found pizza of id '%d'.", id)));
    }

    @Override
    public void updatePizza(
            @NonNull final PizzaDto updatedPizzaDto
    ) throws NotFoundPizzaException, PizzaAlreadyExistsException {
        final PizzaEntity oldPizzaEntity = pizzaRepository.findById(updatedPizzaDto.getId())
                .orElseThrow(() -> new NotFoundPizzaException(
                        String.format("Not found pizza of id '%d'.", updatedPizzaDto.getId()))
                );

        if (!oldPizzaEntity.getName().equals(updatedPizzaDto.getName()) && pizzaRepository.existsByName(updatedPizzaDto.getName())) {
            throw new PizzaAlreadyExistsException(
                    String.format("Pizza with name '%s' already exists.", updatedPizzaDto.getName())
            );
        }

        pizzaRepository.save(pizzaConverter.toEntity(updatedPizzaDto));
    }

    @Override
    public void deletePizzaById(@NonNull final Long id) throws NotFoundPizzaException {
        if (!pizzaRepository.existsById(id)) {
            throw new NotFoundPizzaException(String.format("Not found pizza of id '%d'.", id));
        }

        pizzaRepository.deleteById(id);
    }
}

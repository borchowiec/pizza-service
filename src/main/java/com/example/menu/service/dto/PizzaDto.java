package com.example.menu.service.dto;

import lombok.Value;

import java.util.List;

/**
 * @author Patryk Borchowiec
 */
@Value
public class PizzaDto {
    Long id;
    String name;
    double price;
    List<String> ingredients;
}

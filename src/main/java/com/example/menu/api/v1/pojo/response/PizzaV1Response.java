package com.example.menu.api.v1.pojo.response;

import lombok.Value;

import java.util.List;

/**
 * @author Patryk Borchowiec
 */
@Value
public class PizzaV1Response {
    long id;
    String name;
    double price;
    List<String> ingredients;
}

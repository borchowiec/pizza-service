package com.example.menu.api.v1.pojo.request;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Patryk Borchowiec
 */
@Value
public class UpdatePizzaV1Request {
    @NotBlank
    String name;

    @Min(value = 0)
    double price;

    @NotNull
    List<String> ingredients;
}

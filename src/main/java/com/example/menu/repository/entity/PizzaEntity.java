package com.example.menu.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author Patryk Borchowiec
 */
@Data
@Entity(name = "pizza")
public class PizzaEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    private double price;

    @OneToMany(cascade = CascadeType.ALL)
    private List<IngredientEntity> ingredients;
}

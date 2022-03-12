package com.example.menu.repository.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * @author Patryk Borchowiec
 */
@Data
@Entity(name = "ingredient")
public class IngredientEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
}

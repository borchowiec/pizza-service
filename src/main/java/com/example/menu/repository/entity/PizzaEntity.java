package com.example.menu.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @CreatedDate
    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;
}

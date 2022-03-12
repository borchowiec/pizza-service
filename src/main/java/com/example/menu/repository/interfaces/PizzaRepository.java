package com.example.menu.repository.interfaces;

import com.example.menu.repository.entity.PizzaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Patryk Borchowiec
 */
public interface PizzaRepository extends JpaRepository<PizzaEntity, Long> {
    boolean existsByName(String name);
}

package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkom.backend.entities.Dish;

import java.util.List;
import java.util.Optional;


@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    boolean existsByName(String name);
    Optional<Dish> findDishById(Long dishId);

    List<Dish> findDishesByName(String name);

}


package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkom.backend.entities.Dish;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для взаимодействия с блюдами.
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    /**
     * Проверяет, существует ли блюдо с указанным именем.
     *
     * @param name Имя блюда.
     * @return true, если блюдо с указанным именем существует, в противном случае - false.
     */
    boolean existsByName(String name);

    /**
     * Находит блюдо по его идентификатору.
     *
     * @param dishId Идентификатор блюда.
     * @return Блюдо, если оно существует, иначе - пустой Optional.
     */
    Optional<Dish> findDishById(Long dishId);

    /**
     * Находит блюда по их именам.
     *
     * @param name Имя блюда.
     * @return Список блюд с указанным именем.
     */
    List<Dish> findDishesByName(String name);
}

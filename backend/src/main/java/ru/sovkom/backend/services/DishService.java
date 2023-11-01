package ru.sovkom.backend.services;

import ru.sovkom.backend.entities.Dish;

import java.util.List;

public interface DishService  {

    /**
     * Добавляет блюдо
     *
     * @param dish блюдо
     * @return boolean
     */
    long addDish(Dish dish);

    /**
     * Получает все блюда
     *
     * @return List<Dish>
     */
    List<Dish> getListDishes();

    /**
     * Обновляет блюдо
     *
     * @param dish блюдо
     * @return boolean
     */
    boolean updateDish(Dish dish);

    /**
     * Удаляет блюдо
     *
     * @param dish  блюдо
     * @return boolean
     */
    boolean deleteDish(Dish dish);

    List<Dish> getDishesByName(String name);
}

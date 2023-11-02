package ru.sovkom.backend.services;

import ru.sovkom.backend.entities.Dish;

import java.util.List;

/**
 * Сервис для работы с блюдами.
 */
public interface DishService  {

    /**
     * Добавляет блюдо в базу данных.
     *
     * @param dish Блюдо для добавления.
     * @return Уникальный идентификатор добавленного блюда.
     */
    long addDish(Dish dish);

    /**
     * Получает список всех блюд.
     *
     * @return Список блюд.
     */
    List<Dish> getListDishes();

    /**
     * Удаляет блюдо из базы данных.
     *
     * @param dish Блюдо для удаления.
     * @return `true`, если блюдо успешно удалено, иначе `false`.
     */
    boolean deleteDish(Dish dish);

    /**
     * Получает список блюд по имени.
     *
     * @param name Название блюда или его часть.
     * @return Список блюд, удовлетворяющих критерию поиска.
     */
    List<Dish> getDishesByName(String name);
}

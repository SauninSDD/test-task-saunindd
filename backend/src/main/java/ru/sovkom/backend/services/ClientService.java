package ru.sovkom.backend.services;

import ru.sovkom.backend.entities.CartItem;
import ru.sovkom.backend.entities.Client;
import ru.sovkom.backend.entities.Dish;

import java.util.List;

/**
 * Сервис для взаимодействия с пользователем.
 */
public interface ClientService {
    /**
     * Регистрирует пользователя.
     *
     * @param client Данные о пользователе
     */
    void saveClient(Client client);

    /**
     * Проверяет существует ли пользователь.
     *
     * @param clientId Уникальный идентификатор пользователя
     * @return Возвращает результат проверки
     */
    boolean checkClientExistence(long clientId);

    /**
     * Удаляет пользователя по ID.
     *
     * @param clientId Уникальный идентификатор пользователя
     * @return `true` при успешном удалении и `false`, если пользователя не существует
     */
    boolean deleteClientById(long clientId);

    /**
     * Получает список пользователей по имени.
     *
     * @param name Имя пользователя
     * @return Список пользователей с указанным именем
     */
    List<Client> getUsersByUsername(String name);

    /**
     * Получает список всех пользователей.
     *
     * @return Список всех пользователей
     */
    List<Client> getAllUsers();

    /**
     * Получает корзину пользователя.
     *
     * @param clientId Уникальный идентификатор пользователя
     * @return Список элементов корзины пользователя
     */
    List<CartItem> getClientCart(String clientId);

    /**
     * Добавляет блюдо в избранное пользователя.
     *
     * @param client Пользователь
     * @param dish   Блюдо для добавления в избранное
     * @return `true`, если блюдо успешно добавлено в избранное, иначе `false`
     */
    boolean addDishToFavorites(Client client, Dish dish);
}

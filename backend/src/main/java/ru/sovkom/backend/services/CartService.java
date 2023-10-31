package ru.sovkom.backend.services;

import ru.sovkom.backend.entities.Cart;
import ru.sovkom.backend.entities.CartItem;

import java.util.List;

/**
 * Сервис для взаимодействия с корзиной клиента
 */
public interface CartService {
    /**
     * Добавление блюда в корзину
     *
     * @param cartId Уникальный идентификатор пользователя
     * @param dishId   Уникальный идентификатор блюда
     * @return Возвращает статус добавления блюда в корзину
     */
    boolean addToCart(long cartId, long dishId, int quantity);

    /**
     * Удаление блюда из корзины
     *
     * @param cartId   Уникальный идентификатор корзины
     * @param dishId   Уникальный идентификатор блюда
     * @return Возвращает статус удаления блюда из корзины
     */
    boolean deleteDish(long cartId, long dishId);

    /**
     * Удаляет все блюда из корзины по id клиента
     *
     * @param clientId Уникальный идентификатор клиента
     */
    void deleteAllDish(long clientId);

    /**
     * Изменение количества блюда в корзине
     *
     * @param clientId   Уникальный идентификатор пользователя
     * @param dishId     Уникальный идентификатор блюда
     * @param quantity   Количество добавляемого блюда
     * @return Возвращает статус обновления количества блюда в корзине
     */
    boolean updateDishAmount(long clientId, long dishId, int quantity);

    /**
     * Удаление корзины по ID клиента
     *
     * @param clientId Уникальный идентификатор пользователя
     */
    void deleteCart(long clientId);

    /**
     * Выдает список идентификаторов блюд в корзине пользователя
     *
     * @param cartId Уникальный идентификатор пользователя
     * @return Возвращает список идентификаторов блюд
     */
    List<Long> getListOfDishIdsInCart(long cartId);

    /**
     * Подсчитывает количество блюд в корзине пользователя
     *
     * @param clientId Уникальный идентификатор пользователя
     * @return Возвращает количество товаров
     */
    int countDishesInCart(long clientId);

    /**
     * Получает список элементов корзины клиента
     *
     * @param cartId id корзины
     * @return список элементов корзины
     */
    List<CartItem> getCartItemsByCartId(long cartId);

    List<Cart> getAllCarts();

    Cart getCartIdByClientId(String clientId);

    Cart getCartIdByClientId(long clientId);

}

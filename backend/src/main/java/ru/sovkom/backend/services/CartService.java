package ru.sovkom.backend.services;

import ru.sovkom.backend.entities.Cart;
import ru.sovkom.backend.entities.CartItem;

/**
 * Сервис для взаимодействия с корзиной клиента.
 */
public interface CartService {
    /**
     * Добавление блюда в корзину.
     *
     * @param cartId   Уникальный идентификатор корзины
     * @param dishId   Уникальный идентификатор блюда
     * @param quantity Количество добавляемых блюд
     * @return Возвращает статус добавления блюда в корзину
     */
    boolean addToCart(long cartId, long dishId, int quantity);

    /**
     * Удаление блюда из корзины.
     *
     * @param cart     Корзина клиента
     * @param cartItem Удаляемый элемент корзины
     * @return Возвращает статус удаления блюда из корзины
     */
    boolean deleteDish(Cart cart, CartItem cartItem);

    /**
     * Удаление корзины по ID клиента.
     *
     * @param clientId Уникальный идентификатор пользователя
     */
    void deleteCart(long clientId);

    /**
     * Получение корзины по ID клиента.
     *
     * @param clientId Уникальный идентификатор пользователя
     * @return Объект корзины клиента
     */
    Cart getCartIdByClientId(long clientId);
}

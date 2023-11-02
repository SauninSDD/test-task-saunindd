package ru.sovkom.backend.services;

import ru.sovkom.backend.entities.Order;

import java.util.List;

/**
 * Сервис для работы с заказами.
 */
public interface OrderService {

    /**
     * Создает новый заказ.
     *
     * @param order Заказ для создания.
     */
    void createOrder(Order order);

    /**
     * Удаляет заказ по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор заказа.
     */
    void deleteOrder(long id);

    /**
     * Получает список заказов по трек-номеру заказа.
     *
     * @param orderTrackNumber Номер трека-номер заказа.
     * @return Список заказов с указанным номером трека.
     */
    List<Order> getOrderByOrderTrackNumber(String orderTrackNumber);
}

package ru.sovkom.backend.services;

import ru.sovkom.backend.entities.Order;

import java.util.List;

public interface OrderService {
    boolean createOrder(Order order);

    void deleteOrder(long id);

    List<Order> getOrderByOrderTrackNumber(String orderTrackNumber);

}

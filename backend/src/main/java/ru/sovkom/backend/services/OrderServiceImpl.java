package ru.sovkom.backend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkom.backend.entities.*;
import ru.sovkom.backend.repositories.CartRepository;
import ru.sovkom.backend.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public void createOrder(Order order) {
        if (order.getClient() == null) {
            log.warn("Пользователь не найден");
            return;
        }

        Optional<Cart> checkCartClient = cartRepository.findCartByClient_Id(order.getClient().getId());
        if (checkCartClient.isPresent()) {
            Cart clientCart = checkCartClient.get();
            Client clientOrder = clientCart.getClient();
            order.setClient(clientOrder);

            log.info("Полученная корзина {}", clientCart.getCartItems());
            for (CartItem cartItem : clientCart.getCartItems()) {
                log.info("Блюдо с id {}", cartItem.getId());
                OrderDish orderDish = OrderDish.builder()
                        .order(order)
                        .dish(cartItem.getDish())
                        .orderDishValue(cartItem.getQuantity())
                        .build();
                order.getDishesInOrder().add(orderDish);
            }

            orderRepository.save(order);
            clientCart.getCartItems().clear();
            cartRepository.save(clientCart);
        }
    }

    @Override
    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getOrderByOrderTrackNumber(String orderTrackNumber) {
        if (orderTrackNumber == null || orderTrackNumber.isEmpty()) {
            return orderRepository.findAll();
        } else {
            return orderRepository.findOrderByOrderTrackNumber(orderTrackNumber);
        }
    }
}

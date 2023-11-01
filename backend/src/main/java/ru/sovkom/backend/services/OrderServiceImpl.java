package ru.sovkom.backend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkom.backend.entities.*;
import ru.sovkom.backend.repositories.CartItemRepository;
import ru.sovkom.backend.repositories.CartRepository;
import ru.sovkom.backend.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public boolean createOrder(Order order) {
        Optional<Cart> checkCartClient = cartRepository.findCartByClient_Id(order.getClient().getId());
        if (checkCartClient.isPresent()) {
            Cart clientCart = checkCartClient.get();
            log.info("Полученная корзина {}", clientCart.getCartItems());

            for (CartItem cartItem : clientCart.getCartItems()) {
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
            return true;
        } else {
            return false;
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

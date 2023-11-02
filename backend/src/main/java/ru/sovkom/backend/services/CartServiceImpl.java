package ru.sovkom.backend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkom.backend.entities.Cart;
import ru.sovkom.backend.entities.CartItem;
import ru.sovkom.backend.entities.Dish;
import ru.sovkom.backend.exceptions.CartNotFoundException;
import ru.sovkom.backend.repositories.CartRepository;
import ru.sovkom.backend.repositories.DishRepository;


import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final DishRepository dishRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, DishRepository dishRepository) {
        this.cartRepository = cartRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    public boolean addToCart(long cartId, long dishId, int quantity) {
        Optional<Cart> checkCartClient = cartRepository.findById(cartId);
        if (checkCartClient.isPresent()) {
            Cart cartClient = checkCartClient.get();
            Optional<CartItem> cartItem = cartClient.getCartItems().stream()
                    .filter(item -> item.getDish().getId() == dishId)
                    .findFirst();
            if (cartItem.isPresent()) {
                cartItem.get().setQuantity(quantity);
            } else {
                Optional<Dish> checkDish = dishRepository.findDishById(dishId);
                if (checkDish.isPresent()) {
                    Dish dish = checkDish.get();
                    CartItem newCartItem = CartItem.builder()
                            .cart(cartClient)
                            .dish(dish)
                            .quantity(quantity)
                            .build();
                    cartClient.getCartItems().add(newCartItem);
                } else {
                    return false;
                }
            }
            cartRepository.save(cartClient);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteDish(Cart cart, CartItem cartItem) {
        log.info("Сервер: Удаление блюда {} из корзины {} ", cartItem, cart);
        cart.getCartItems().remove(cartItem);
        cartRepository.save(cart);
        return true;
    }

    @Override
    @Transactional
    public void deleteCart(long clientId) {
        log.info("Удаление корзины пользователя с ID {}", clientId);
        cartRepository.deleteCartByClientId(clientId);
    }

    @Override
    public Cart getCartIdByClientId(long clientId) {
        Optional<Cart> cart = cartRepository.findCartByClient_Id(clientId);
        return cart.orElseThrow(() -> new CartNotFoundException("Корзина не найдена для клиента с ID: " + clientId));
    }

}

package ru.sovkom.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkom.backend.entities.Cart;
import ru.sovkom.backend.entities.CartItem;
import ru.sovkom.backend.entities.Client;
import ru.sovkom.backend.entities.Dish;
import ru.sovkom.backend.exceptions.CartNotFoundException;
import ru.sovkom.backend.exceptions.EmptyCartIdException;
import ru.sovkom.backend.repositories.CartItemRepository;
import ru.sovkom.backend.repositories.CartRepository;
import ru.sovkom.backend.repositories.ClientRepository;
import ru.sovkom.backend.repositories.DishRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ClientRepository clientRepository;
    private final CartItemRepository cartItemRepository;

    private final DishRepository dishRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ClientRepository clientRepository, CartItemRepository cartItemRepository, DishRepository dishRepository) {
        this.cartRepository = cartRepository;
        this.clientRepository = clientRepository;
        this.cartItemRepository = cartItemRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    public boolean addToCart(long cartId, long dishId, int quantity) {
        Optional<Cart> checkCartClient = cartRepository.findById(cartId);
        Cart cartClient = checkCartClient.orElse(null);
        if (checkCartClient.isPresent()) {
            Optional<CartItem> cartItem = cartClient.getCartItems().stream()
                    .filter(item -> item.getDish().getId() == dishId)
                    .findFirst();
            if (cartItem.isPresent()) {
                cartItem.get().setQuantity(quantity);
            } else {
                Optional<Dish> checkDish = dishRepository.findDishById(dishId);
                Dish dish = checkDish.orElse(null);
                if (checkDish.isPresent()) {
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
    public boolean deleteDish(long cartId, long dishId) {
        cartItemRepository.deleteCartItemByCartIdAndDishId(cartId, dishId);
        return true;
    }

    @Override
    @Transactional
    public void deleteAllDish(long clientId) {
        cartItemRepository.deleteAll();
    }

    @Override
    public boolean updateDishAmount(long clientId, long dishId, int quantity) {
        Optional<Cart> cart = cartRepository.findCartByClient_Id(clientId);

        if (cart.isPresent()) {
            Cart shoppingCart = cart.get();
            List<CartItem> cartItems = shoppingCart.getCartItems();

            for (CartItem cartItem : cartItems) {
                if (cartItem.getDish().getId() == dishId) {
                    cartItem.setQuantity(quantity);
                    cartRepository.save(shoppingCart);

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void deleteCart(long clientId) {
        Optional<Cart> cart = cartRepository.findCartByClient_Id(clientId);

        if (cart.isPresent()) {
            long cartId = cart.get().getId();
            cartRepository.deleteById(cartId);
        }
    }

    @Override
    public List<Long> getListOfDishIdsInCart(long cartId) {
        Optional<Cart> cart = cartRepository.findCartByClient_Id(cartId);

        if (cart.isPresent()) {
            Cart shoppingCart = cart.get();
            List<CartItem> cartItems = shoppingCart.getCartItems();
            List<Long> dishIds = new ArrayList<>();

            for (CartItem cartItem : cartItems) {
                dishIds.add(cartItem.getDish().getId());
            }
            return dishIds;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CartItem> getCartItemsByCartId(long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Cart getCartIdByClientId(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            throw new EmptyCartIdException("Empty cart ID");
        } else {
            long longIdClient = Long.parseLong(clientId, 10);
            Optional<Cart> cart = cartRepository.findCartByClient_Id(longIdClient);
            return cart.orElseThrow(() -> new CartNotFoundException("Cart not found for client ID: " + clientId));
        }
    }

    @Override
    public Cart getCartIdByClientId(long clientId) {

            Optional<Cart> cart = cartRepository.findCartByClient_Id(clientId);
            return cart.orElseThrow(() -> new CartNotFoundException("Cart not found for client ID: " + clientId));

    }


    @Override
    public int countDishesInCart(long clientId) {
        Optional<Cart> cart = cartRepository.findCartByClient_Id(clientId);

        if (cart.isPresent()) {
            Cart shoppingCart = cart.get();
            List<CartItem> cartItems = shoppingCart.getCartItems();
            int totalQuantity = 0;

            for (CartItem cartItem : cartItems) {
                totalQuantity += cartItem.getQuantity();
            }

            return totalQuantity;
        } else {

            return 0;
        }
    }
}

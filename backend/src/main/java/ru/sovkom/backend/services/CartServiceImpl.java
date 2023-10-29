package ru.sovkom.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkom.backend.entities.Cart;
import ru.sovkom.backend.entities.CartItem;
import ru.sovkom.backend.entities.Client;
import ru.sovkom.backend.entities.Dish;
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
    public boolean addToCart(long cartId, long dishId) {
        Optional<Cart> cart = cartRepository.findCartById(cartId);


        Cart shoppingCart = cart.orElseGet(() -> {
            Optional<Client> user = clientRepository.findById(cartId);
            if (user.isPresent()) {
                Cart newCart = new Cart();
                newCart.setClient(user.get());

                return cartRepository.save(newCart);
            }

            return null;
        });

        if (shoppingCart != null) {
            Optional<CartItem> cartItem = shoppingCart.getCartItems().stream()
                    .filter(item -> item.getDish().getId() == dishId)
                    .findFirst();

            if (cartItem.isPresent()) {
                CartItem existingCartItem = cartItem.get();
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setCart(shoppingCart);

                Optional<Dish> dish = dishRepository.findDishById(dishId);
                Dish dishInCart = dish.orElse(null);
                if (dish.isPresent()) {
                    newCartItem.setDish(dishInCart);
                    newCartItem.setQuantity(1);
                    shoppingCart.getCartItems().add(newCartItem);
                } else {
                    return false;
                }
            }

            cartRepository.save(shoppingCart);

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
        Optional<Cart> cart = cartRepository.findCartById(clientId);

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
    public void deleteCart(long clientId) {
        Optional<Cart> cart = cartRepository.findCartById(clientId);

        if (cart.isPresent()) {
            long cartId = cart.get().getId();
            cartItemRepository.deleteAll();
            cartRepository.deleteById(cartId);
        }
    }

    @Override
    public List<Long> getListOfDishIdsInCart(long cartId) {
        Optional<Cart> cart = cartRepository.findCartById(cartId);

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
    public int countDishesInCart(long clientId) {
        Optional<Cart> cart = cartRepository.findCartById(clientId);

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

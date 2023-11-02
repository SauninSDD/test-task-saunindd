package ru.sovkom.backend.services;

import lombok.extern.slf4j.Slf4j;
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

import java.util.*;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, CartService cartService, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.clientRepository = clientRepository;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public void saveClient(Client client) {
        log.info("Сохранение данных клиента{}", client);
        Client savedClient = clientRepository.save(client);
        Optional<Cart> checkCart = cartRepository.findCartByClient_Id(client.getId());
        if (checkCart.isEmpty()) {
            Cart newCart = Cart.builder()
                    .client(savedClient)
                    .build();
            cartRepository.save(newCart);
        }
    }


    @Override
    public boolean checkClientExistence(long clientId) {

        return clientRepository.existsById(clientId);
    }

    @Override
    @Transactional
    public boolean deleteClientById(long clientId) {
        if (checkClientExistence(clientId)) {
            cartService.deleteCart(clientId);
            clientRepository.deleteById(clientId);
            return true;
        }
        return false;
    }



    @Override
    public List<Client> getUsersByUsername(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return clientRepository.findAll();
        } else {
            return clientRepository.findUsersByUsername(stringFilter);
        }
    }

    @Override
    public List<Client> getAllUsers() {
        return clientRepository.findAll();
    }


    @Override
    public List<CartItem> getClientCart(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            return cartItemRepository.findAll();
        } else {
            long longIdClient = Long.parseLong(clientId, 10);
            log.info("Получаем корзину клиента с id {}", longIdClient);
            Optional<Cart> checkCart = cartRepository.findCartByClient_Id(longIdClient);
            if (checkCart.isPresent()) {
                log.info(" корзина {}", checkCart.get());
                return cartItemRepository.findByCartId(checkCart.get().getId());
            }
            return cartItemRepository.findAll();
        }
    }

    public boolean addDishToFavorites(Client client, Dish dish) {
        log.info("Добавление избранных блюд вызвалось");
        Set<Dish> favoriteDishes = client.getDishesFavorites();

        boolean isDishInFavorites = favoriteDishes.stream()
                .anyMatch(favoriteDish -> Objects.equals(favoriteDish.getId(), dish.getId()));

        if (!isDishInFavorites) {
            favoriteDishes.add(dish);
            log.info("Блюдо с id {} добавлено в избранное", dish.getId());
            clientRepository.save(client);
            return true;
        } else {
            log.info("Блюдо уже находится в избранном у клиента");
            return false;
        }
    }
}

package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkom.backend.entities.Cart;

import java.util.Optional;

/**
 * Репозиторий для взаимодействия с корзиной клиента.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Поиск корзины клиента по идентификатору клиента.
     *
     * @param clientId Идентификатор клиента.
     * @return Объект Optional, содержащий корзину клиента, если она существует.
     */
    Optional<Cart> findCartByClient_Id(long clientId);
}

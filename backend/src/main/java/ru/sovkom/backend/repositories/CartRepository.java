package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkom.backend.entities.Cart;

import java.util.Optional;

/**
 * Репозиторий для взаимодействия с корзиной клиента
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findCartById(long cartId);
}

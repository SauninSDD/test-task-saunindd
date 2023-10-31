package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkom.backend.entities.CartItem;

import java.util.List;

/**
 * Репозиторий для взаимодействия с элементами корзины клиента
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /**
     * Получает список элементов корзины клиента
     *
     * @param cartId id корзины
     * @return список элементов корзины
     */
    List<CartItem> findByCartId(long cartId);

    /**
     * Удаляет блюдо из корзины
     *
     * @param cartId id корзины
     * @param dishId id блюда
     */
    void deleteCartItemByCartIdAndDishId(long cartId, long dishId);

    void deleteAllByCart_Id(long cartId);
}


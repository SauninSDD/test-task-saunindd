package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sovkom.backend.entities.Order;

import java.util.List;

/**
 * Репозиторий для взаимодействия с заказами.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Находит заказы по их номеру отслеживания.
     *
     * @param orderTrackNumber Номер отслеживания заказа.
     * @return Список заказов с указанным номером отслеживания.
     */
    List<Order> findOrderByOrderTrackNumber(String orderTrackNumber);
}

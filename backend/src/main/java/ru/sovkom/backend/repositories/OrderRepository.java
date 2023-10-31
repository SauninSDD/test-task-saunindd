package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sovkom.backend.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findOrderByOrderTrackNumber(String orderTrackNumber);
}

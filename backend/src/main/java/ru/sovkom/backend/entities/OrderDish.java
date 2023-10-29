package ru.sovkom.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Embeddable
@Table(name = "orders_dishes")
@Data
@NoArgsConstructor
public class OrderDish {
    @Id
    @Column(name = "id_order_dish")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_order")
    private Order order;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_dish")
    private Dish dish;

    @Column
    private byte orderDishValue;



}

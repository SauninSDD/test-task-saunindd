package ru.sovkom.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Сущность, представляющая блюдо в заказе.
 */
@Entity
@Embeddable
@Table(name = "orders_dishes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDish {
    /**
     * Уникальный идентификатор записи о блюде в заказе.
     */
    @Id
    @Column(name = "id_order_dish")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Заказ, к которому относится данное блюдо.
     */
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_order")
    private Order order;

    /**
     * Блюдо, которое включено в заказ.
     */
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_dish")
    private Dish dish;

    /**
     * Количество данного блюда в заказе.
     */
    @Column
    private int orderDishValue;
}

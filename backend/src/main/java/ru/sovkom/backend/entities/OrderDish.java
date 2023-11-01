package ru.sovkom.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Embeddable
@Table(name = "orders_dishes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDish {
    @Id
    @Column(name = "id_order_dish")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_order")
    private Order order;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_dish")
    private Dish dish;

    @Column
    private int orderDishValue;



}

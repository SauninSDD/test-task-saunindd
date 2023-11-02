package ru.sovkom.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность блюда в меню ресторана.
 */
@Data
@Table(name = "dishes")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    /**
     * Уникальный идентификатор блюда.
     */
    @Id
    @Column(name = "id_dish")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование блюда.
     */
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Цена блюда.
     */
    @Column(nullable = false)
    private Double price;

    /**
     * Список заказов, в которых используется данное блюдо.
     */
    @OneToMany(mappedBy = "dish", fetch = FetchType.EAGER)
    private List<OrderDish> dishesInOrder = new ArrayList<>();

    @Override
    public String toString() {
        return name;
    }
}

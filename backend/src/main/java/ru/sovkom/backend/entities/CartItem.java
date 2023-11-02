package ru.sovkom.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Сущность элемента корзины.
 */
@Data
@Builder
@Entity
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    /**
     * Уникальный идентификатор элемента корзины.
     */
    @Id
    @Column(name = "id_cartItem")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Связь с корзиной, к которой принадлежит элемент.
     */
    @ManyToOne
    @JoinColumn(name = "id_cart", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Cart cart;

    /**
     * Связь с блюдом, которое представляет данный элемент корзины.
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_dish")
    private Dish dish;

    /**
     * Количество данного блюда в корзине.
     */
    @Column(nullable = false)
    private int quantity;


    @Override
    public String toString() {
        return "CartItem{id=" + id + ", quantity='" + quantity + "'}";
    }
}

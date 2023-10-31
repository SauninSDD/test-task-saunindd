package ru.sovkom.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

/**
 * Сущность корзины клиента
 */
@Data
@Builder
@Table(name = "carts")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @Column(name = "id_cart")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> cartItems;

    @Override
    public String toString() {
        return id.toString();
    }
}

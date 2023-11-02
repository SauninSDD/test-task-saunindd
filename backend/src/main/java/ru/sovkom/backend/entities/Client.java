package ru.sovkom.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность клиента ресторана.
 */
@Data
@Builder
@Table(name = "clients")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    /**
     * Уникальный идентификатор клиента.
     */
    @Id
    @Column(name = "id_client")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Имя пользователя клиента.
     */
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Номер телефона клиента.
     */
    @Size(max = 100)
    @Column(nullable = false)
    private String number;

    /**
     * Электронная почта клиента.
     */
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    /**
     * Пароль клиента.
     */
    @Size(max = 100)
    @Column(nullable = false)
    private String password;

    /**
     * Множество блюд, добавленных клиентом в избранное.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "clients_favorites",
            joinColumns = @JoinColumn(name = "id_client"),
            inverseJoinColumns = @JoinColumn(name = "id_dish"))
    private Set<Dish> dishesFavorites = new HashSet<>();
}
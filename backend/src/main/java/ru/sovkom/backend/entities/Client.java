package ru.sovkom.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Сущность клиента ресторана
 */
@Data
@Builder
@Table(name = "clients")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @Column(name = "id_client")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 100)
    @Column(nullable = false)
    private String username;

    @Size(max = 100)
    @Column
    private String number;


    @Size(max = 100)
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Size(max = 100)
    @Column(nullable = false)
    private String password;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "clients_favorites",
            joinColumns = @JoinColumn(name = "id_client"),
            inverseJoinColumns = @JoinColumn(name = "id_dish"))
    private Set<Dish> dishesFavorites = new HashSet<>();




}

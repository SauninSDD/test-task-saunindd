package ru.sovkom.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@Table(name = "dishes")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    @Id
    @Column(name = "id_dish")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double price;

    @OneToMany(mappedBy = "dish", fetch = FetchType.EAGER)
    private Set<OrderDish> dishesInOrder = new HashSet<>();

    @Override
    public String toString() {
        return name;
    }
}

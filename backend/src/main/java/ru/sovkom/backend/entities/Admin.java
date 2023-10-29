package ru.sovkom.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "admins")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @Column(name = "id_admin")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Size(max = 100)
    @Column(nullable = false)
    private String password;
}

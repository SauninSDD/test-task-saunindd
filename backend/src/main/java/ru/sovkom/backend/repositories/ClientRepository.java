package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sovkom.backend.entities.Client;
import ru.sovkom.backend.entities.Dish;

import java.util.Optional;
import java.util.List;

/**
 * Репозиторий для взаимодействия с клиентом
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {



    /**
     * Ищет клиента по имени
     *
     * @param username имя клиента
     * @return пользователь
     */
    Optional<Client> findByUsername(String username);

    /**
     * Ищет клиента по email
     *
     * @param email электронная почта клиента
     * @return пользователь
     */
    Optional<Client> findUserByEmail(String email);

    /**
     * Проверяет существует ли пользователь с указанным email
     *
     * @param email электронная почта клиента
     * @return true, если пользователь существует
     */
    Boolean existsByEmail(String email);

    List<Client> findUsersByUsername(String name);

    @Query("SELECT COUNT(c) FROM Client c JOIN c.dishesFavorites df WHERE :dish IN elements(df)")
    long countClientsWithFavoriteDish(@Param("dish") Dish dish);




}

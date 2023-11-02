package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sovkom.backend.entities.Client;
import ru.sovkom.backend.entities.Dish;

import java.util.List;

/**
 * Репозиторий для взаимодействия с клиентом.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Находит клиентов по имени.
     *
     * @param name Имя клиента.
     * @return Список клиентов с указанным именем.
     */
    List<Client> findUsersByUsername(String name);

    /**
     * Подсчитывает количество клиентов, у которых указанное блюдо находится в списке избранных.
     *
     * @param dish Блюдо для проверки.
     * @return Количество клиентов с указанным блюдом в списке избранных.
     */
    @Query("SELECT COUNT(c) FROM Client c JOIN c.dishesFavorites df WHERE :dish IN elements(df)")
    long countClientsWithFavoriteDish(@Param("dish") Dish dish);
}

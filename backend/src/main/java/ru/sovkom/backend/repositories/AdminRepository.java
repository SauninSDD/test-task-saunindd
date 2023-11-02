package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkom.backend.entities.Admin;

import java.util.Optional;

/**
 * Репозиторий для работы с администраторами.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Поиск админа по указанной электронной почте и паролю.
     *
     * @param email    Электронная почта админа.
     * @param password Пароль админа.
     * @return Объект Optional, содержащий найденного админа, если такой админ существует.
     */
    Optional<Admin> findAdminByEmailAndPassword(String email, String password);
}

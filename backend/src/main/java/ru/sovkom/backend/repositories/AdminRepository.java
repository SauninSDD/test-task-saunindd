package ru.sovkom.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkom.backend.entities.Admin;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * Ищет админа по email и паролю
     *
     * @param email электронная почта клиента
     * @param password пароль клиента
     * @return пользователя
     */
    Optional<Admin> findAdminByEmailAndPassword(String email, String password);
}

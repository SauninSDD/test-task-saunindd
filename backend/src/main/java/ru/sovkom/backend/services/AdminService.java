package ru.sovkom.backend.services;

import ru.sovkom.backend.entities.Admin;

import java.util.Optional;


public interface AdminService {

    /**
     * Регистрирует админа
     *
     * @param admin Данные о админе
     * @return Возвращает идентификатор созданного пользователя
     */
    boolean signUp(Admin admin);

    Optional<Admin> getAdminByEmailAndPassword(String email, String password);
}

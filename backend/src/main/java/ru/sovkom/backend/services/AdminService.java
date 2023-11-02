package ru.sovkom.backend.services;

import ru.sovkom.backend.entities.Admin;

import java.util.Optional;

/**
 * Сервис для работы с администраторами системы.
 */
public interface AdminService {

    /**
     * Регистрирует админа.
     *
     * @param admin Данные о админе.
     * @return `true`, если админ успешно зарегистрирован, в противном случае `false`.
     */
    boolean signUp(Admin admin);

    /**
     * Получает админа по электронной почте и паролю.
     *
     * @param email    Электронная почта админа.
     * @param password Пароль админа.
     * @return Админ, если совпадение найдено, иначе `Optional.empty()`.
     */
    Optional<Admin> getAdminByEmailAndPassword(String email, String password);
}

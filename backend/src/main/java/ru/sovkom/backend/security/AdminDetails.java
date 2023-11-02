package ru.sovkom.backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.sovkom.backend.entities.Admin;

import java.util.Collection;
import java.util.Collections;

/**
 * Реализация интерфейса UserDetails для администратора.
 */
public class AdminDetails implements UserDetails {
    private Admin admin;

    public AdminDetails(Admin admin) {
        this.admin = admin;
    }

    /**
     * Получает коллекцию ролей администратора.
     *
     * @return Пустая коллекция ролей.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * Получает пароль администратора.
     *
     * @return Пароль администратора.
     */
    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    /**
     * Получает имя администратора (email).
     *
     * @return Имя администратора (email).
     */
    @Override
    public String getUsername() {
        return admin.getEmail();
    }

    /**
     * Проверяет, не истек ли срок действия учетной записи.
     *
     * @return Всегда возвращает `true`.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирована ли учетная запись администратора.
     *
     * @return Всегда возвращает `true`.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных администратора.
     *
     * @return Всегда возвращает `true`.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, активен ли администратор.
     *
     * @return Всегда возвращает `true`.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

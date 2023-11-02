package ru.sovkom.backend.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Сервис безопасности для работы с аутентификацией и выходом из системы.
 */
@Component
public class SecurityService {
    private final AuthenticationContext authenticationContext;

    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    /**
     * Получает аутентифицированного пользователя.
     *
     * @return Детали аутентифицированного пользователя.
     */
    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
    }

    /**
     * Выход из системы текущего пользователя.
     */
    public void logout() {
        authenticationContext.logout();
    }
}

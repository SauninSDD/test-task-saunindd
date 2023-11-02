package ru.sovkom.backend.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.sovkom.backend.views.authorization.LoginView;

/**
 * Конфигурация безопасности для приложения.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private final AdminAuthenticationProvider adminAuthenticationProvider;

    public SecurityConfig(AdminAuthenticationProvider adminAuthenticationProvider) {
        this.adminAuthenticationProvider = adminAuthenticationProvider;
    }

    /**
     * Настройка безопасности HTTP-запросов.
     *
     * @param http Конфигурация безопасности HTTP-запросов.
     * @throws Exception Исключение, если настройка не может быть выполнена.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png")).permitAll());

        http.authenticationProvider(adminAuthenticationProvider);
        super.configure(http);
        setLoginView(http, LoginView.class);
    }
}

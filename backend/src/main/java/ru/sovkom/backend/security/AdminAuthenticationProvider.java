package ru.sovkom.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.sovkom.backend.entities.Admin;
import ru.sovkom.backend.services.AdminService;

import java.util.Optional;

@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {

    private final AdminService adminService;

    @Autowired
    public AdminAuthenticationProvider(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<Admin> admin = adminService.getAdminByEmailAndPassword(username, password);

        if (admin.isPresent()) {
            if (admin.get().getPassword().equals(password)) {
                AdminDetails adminDetails = new AdminDetails(admin.get());
                return new UsernamePasswordAuthenticationToken(adminDetails, password, adminDetails.getAuthorities());
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

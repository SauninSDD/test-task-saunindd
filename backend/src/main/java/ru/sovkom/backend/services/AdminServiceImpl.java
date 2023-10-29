package ru.sovkom.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sovkom.backend.entities.Admin;
import ru.sovkom.backend.repositories.AdminRepository;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService{

    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public boolean signUp(Admin admin) {
        Optional<Admin> isExistAdmin = adminRepository.findAdminByEmailAndPassword(admin.getEmail(), admin.getPassword());
        if(isExistAdmin.isPresent()){
            return false;
        } else {
            adminRepository.save(admin);
            return true;
        }
    }

    @Override
    public Optional<Admin> getAdminByEmailAndPassword(String email, String password) {
        return adminRepository.findAdminByEmailAndPassword(email, password);
    }
}

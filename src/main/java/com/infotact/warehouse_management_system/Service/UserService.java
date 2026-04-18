package com.infotact.warehouse_management_system.Service;

import com.infotact.warehouse_management_system.DTO.Request.RegisterRequest;
import com.infotact.warehouse_management_system.Enum.Role;
import com.infotact.warehouse_management_system.Model.User;
import com.infotact.warehouse_management_system.Repository.UserRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(RegisterRequest req) {

        // Duplicate check
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Role validation
        Role role;
        try {
            role = Role.valueOf(req.getRole().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid role. Use ADMIN or OPERATOR");
        }

        // Create user
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword())); // encode
        user.setRole(role);

        userRepo.save(user);

        return "User registered successfully";
    }

    @Transactional
    public User getUserByUsername(String username) {

        return userRepo.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }
}

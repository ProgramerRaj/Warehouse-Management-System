package com.infotact.warehouse_management_system.Controller;

import com.infotact.warehouse_management_system.Components.JwtUtil;
import com.infotact.warehouse_management_system.DTO.Request.LoginRequest;
import com.infotact.warehouse_management_system.DTO.Request.RegisterRequest;
import com.infotact.warehouse_management_system.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        // GET USER FROM DB
        var user = userService.getUserByUsername(request.getUsername());

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name() // correct role
        );

        return ResponseEntity.ok(Map.of(
                "token", token,
                "message", "Login successful"
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterRequest req) {
        return ResponseEntity.ok(userService.register(req));
    }
}

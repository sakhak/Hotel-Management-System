package com.sakhak.hotel.auth;

import com.sakhak.hotel.roles.Role;
import com.sakhak.hotel.roles.RoleRepository;
import com.sakhak.hotel.security.JwtService;
import com.sakhak.hotel.users.User;
import com.sakhak.hotel.users.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================
    // REGISTER CUSTOMER
    // =========================

    public Map<String, Object> register(
            RegisterRequest request
    ) {

        if (request.getUsername() == null
                || request.getUsername().isBlank()) {

            throw new RuntimeException(
                    "Username is required"
            );
        }

        if (request.getEmail() == null
                || request.getEmail().isBlank()) {

            throw new RuntimeException(
                    "Email is required"
            );
        }

        if (request.getPassword() == null
                || request.getPassword().isBlank()) {

            throw new RuntimeException(
                    "Password is required"
            );
        }

        if (request.getPassword().length() < 8) {

            throw new RuntimeException(
                    "Password must be at least 8 characters"
            );
        }

        if (userRepository.existsByUsername(
                request.getUsername()
        )) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(
                request.getEmail()
        )) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        Role customerRole =
                roleRepository.findByName("CUSTOMER")
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "CUSTOMER role not found"
                                )
                        );

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(customerRole);
        user.setEnabled(true);

        User savedUser =
                userRepository.save(user);

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "message",
                "Registration successful"
        );

        response.put("id", savedUser.getId());
        response.put(
                "username",
                savedUser.getUsername()
        );
        response.put(
                "email",
                savedUser.getEmail()
        );
        response.put(
                "role",
                savedUser.getRole().getName()
        );

        return response;
    }

    // =========================
    // LOGIN
    // =========================

    public Map<String, Object> login(
            LoginRequest request
    ) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .or(() ->
                        userRepository.findByEmail(
                                request.getUsername()
                        )
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username/email or password"
                        )
                );

        if (!Boolean.TRUE.equals(
                user.getEnabled()
        )) {

            throw new RuntimeException(
                    "Account is disabled"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                    "Invalid username/email or password"
            );
        }

        String token =
                jwtService.generateToken(user);

        Map<String, Object> userData =
                new LinkedHashMap<>();

        userData.put("id", user.getId());
        userData.put(
                "username",
                user.getUsername()
        );
        userData.put(
                "email",
                user.getEmail()
        );
        userData.put(
                "roleId",
                user.getRole().getId()
        );
        userData.put(
                "role",
                user.getRole().getName()
        );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "message",
                "Login successful"
        );

        response.put("token", token);
        response.put("user", userData);

        return response;
    }
}
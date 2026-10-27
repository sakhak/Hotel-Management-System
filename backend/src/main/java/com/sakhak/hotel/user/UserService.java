package com.sakhak.hotel.users;

import com.sakhak.hotel.roles.Role;
import com.sakhak.hotel.roles.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET ALL USERS
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // GET USER BY ID
    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id)
                );

        return convertToDTO(user);
    }

    // CREATE USER
    public UserDTO createUser(UserDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role not found with id: " + dto.getRoleId()
                        )
                );

        User user = new User();

        user.setRole(role);
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        user.setEnabled(
                dto.getEnabled() != null
                        ? dto.getEnabled()
                        : true
        );

        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    // UPDATE USER
    public UserDTO updateUser(Long id, UserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update role
        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            user.setRole(role);
        }

        // Update username
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }

        // Update email
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        // Update password
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Update enabled
        if (dto.getEnabled() != null) {
            user.setEnabled(dto.getEnabled());
        }

        User updatedUser = userRepository.save(user);

        return convertToDTO(updatedUser);
    }

    // DELETE USER
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException(
                    "User not found with id: " + id
            );
        }

        userRepository.deleteById(id);
    }

    // ENTITY → DTO
    private UserDTO convertToDTO(User user) {

        return new UserDTO(
                user.getId(),
                user.getRole().getId(),
                user.getRole().getName(),
                user.getUsername(),
                user.getEmail(),

                // Don't return password
                null,

                user.getEnabled()
        );
    }
}
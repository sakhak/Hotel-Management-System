package com.sakhak.hotel.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    // CREATE USER
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestBody UserDTO dto
    ) {

        UserDTO createdUser = userService.createUser(dto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "User created successfully");
        response.put("user", createdUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO dto
    ) {

        UserDTO updatedUser = userService.updateUser(id, dto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "User updated successfully");
        response.put("user", updatedUser);

        return ResponseEntity.ok(response);
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "User deleted successfully");

        return ResponseEntity.ok(response);
    }
}
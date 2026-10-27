package com.sakhak.hotel.roles;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // GET all roles
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {

        return ResponseEntity.ok(
                roleService.getAllRoles()
        );
    }

    // GET role by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                roleService.getRoleById(id)
        );
    }

    // CREATE role
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRole(
            @RequestBody RoleDTO dto
    ) {

        RoleDTO createdRole = roleService.createRole(dto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Role created successfully");
        response.put("role", createdRole);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // UPDATE role
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRole(
            @PathVariable Long id,
            @RequestBody RoleDTO dto
    ) {

        RoleDTO updatedRole = roleService.updateRole(id, dto);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Role updated successfully");
        response.put("role", updatedRole);

        return ResponseEntity.ok(response);
    }

    // DELETE role
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRole(
            @PathVariable Long id
    ) {

        roleService.deleteRole(id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Role deleted successfully");

        return ResponseEntity.ok(response);
    }
}
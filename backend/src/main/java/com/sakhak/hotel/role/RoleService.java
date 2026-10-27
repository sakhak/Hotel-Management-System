package com.sakhak.hotel.roles;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Get all roles
    public List<RoleDTO> getAllRoles() {

        return roleRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Get role by ID
    public RoleDTO getRoleById(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with id: " + id)
                );

        return convertToDTO(role);
    }

    // Create role
    public RoleDTO createRole(RoleDTO dto) {

        if (roleRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Role already exists: " + dto.getName());
        }

        Role role = new Role();

        role.setName(dto.getName());
        role.setDescription(dto.getDescription());

        Role savedRole = roleRepository.save(role);

        return convertToDTO(savedRole);
    }

    // Update role
    public RoleDTO updateRole(Long id, RoleDTO dto) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with id: " + id)
                );

        if (!role.getName().equals(dto.getName())
                && roleRepository.existsByName(dto.getName())) {

            throw new RuntimeException("Role already exists: " + dto.getName());
        }

        role.setName(dto.getName());
        role.setDescription(dto.getDescription());

        Role updatedRole = roleRepository.save(role);

        return convertToDTO(updatedRole);
    }

    // Delete role
    public void deleteRole(Long id) {

        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }

        roleRepository.deleteById(id);
    }

    // Convert Entity → DTO
    private RoleDTO convertToDTO(Role role) {

        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }
}